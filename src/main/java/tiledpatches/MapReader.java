/*-
 * #%L
 * This file is part of libtiled-java.
 * %%
 * Copyright (C) 2004 - 2017 Thorbjørn Lindeijer <thorbjorn@lindeijer.nl>
 * Copyright (C) 2004 - 2017 Adam Turk <aturk@biggeruniverse.com>
 * Copyright (C) 2016 - 2017 Mike Thomas <mikepthomas@outlook.com>
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */
package tiledpatches;

import org.mapeditor.core.*;
import org.mapeditor.core.Tile;
import org.mapeditor.core.TileLayer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

/**
 * The standard map reader for TMX files. Supports reading .tmx, .tmx.gz and
 * *.tsx files.
 *
 * @author Mike Thomas
 * @version 1.0.2
 */
public class MapReader {

    /**
     * <p>readMap.</p>
     *
     * @param in a {@link InputStream} object.
     * @param xmlPath a {@link String} object.
     * @return a {@link org.mapeditor.core.Map} object.
     * @throws IOException if any.
     */
    public Map readMap(InputStream in, String xmlPath) throws IOException {
        Map unmarshalledMap = unmarshal(in, Map.class);
        return buildMap(unmarshalledMap, xmlPath);
    }

    /**
     * <p>readMap.</p>
     *
     * @param in a {@link InputStream} object.
     * @param xmlPath a {@link String} object.
     * @return a {@link org.mapeditor.core.Map} object.
     * @throws IOException if any.
     */
    public Map readMap(InputStream in, Class resourceGrabber) throws IOException {
        Map unmarshalledMap = unmarshal(in, Map.class);
        return buildMap(unmarshalledMap, resourceGrabber);
    }

    /**
     * Reads the map from the specified web URL, and uses nested web URLs for loading a Tiled map.
     * @param in The URL representing the game map file.
     * @return The loaded {@link org.mapeditor.core.Map} object.
     * @throws IOException if any, especially if the URL cannot be reached.
     *
     * @author Jacob Ekstrum
     */
    public Map readMap(URL in) throws IOException {
        Map unmarshalledMap = unmarshal(in, Map.class);
        unmarshalledMap = buildMap(unmarshalledMap);
        for (MapLayer mm : unmarshalledMap) {
            TileLayer l = (TileLayer)mm;
            if (l.tileMap == null) {
                l.tileMap = new Tile[l.getHeight()][l.getWidth()];
                String dt = l.getData().getValue();
                String[] rows = dt.split(" ");
                for (int i = 0; i < rows.length; i++) {
                    String[] addable = rows[i].split(",");
                    for (int j = 0; j < addable.length; j++) {
                        Integer tle = Integer.parseInt(addable[j]) - 1;
                        for (TileSet ts : unmarshalledMap.getTileSets()) {
                            if (ts.getFirstgid() <= tle && tle < ts.getFirstgid() + ts.getInternalTiles().size()) {
                                l.tileMap[i][j] = ts.getTile(tle);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return unmarshalledMap;
    }

    /**
     * <p>readMap.</p>
     *
     * @param filename a {@link String} object.
     * @return a {@link org.mapeditor.core.Map} object.
     * @throws IOException if any.
     */
    public Map readMap(String filename) throws IOException {
        int fileSeparatorIndex = filename.lastIndexOf(File.separatorChar) + 1;
        String xmlPath = makeUrl(filename.substring(0, fileSeparatorIndex));

        String xmlFile = makeUrl(filename);

        URL url = new URL(xmlFile);
        InputStream is = url.openStream();

        // Wrap with GZIP decoder for .tmx.gz files
        if (filename.endsWith(".gz")) {
            is = new GZIPInputStream(is);
        }

        return readMap(is, xmlPath);
    }

    /**
     * <p>readTileset.</p>
     *
     * @param in a {@link InputStream} object.
     * @return a {@link org.mapeditor.core.TileSet} object.
     */
    public TileSet readTileset(InputStream in) {
        return unmarshal(in, TileSet.class);
    }

    public TileSet readTileset(URL in) {
        try {
            TileSet temporary = unmarshal(in.openStream(), TileSet.class);
            return temporary;
        } catch (IOException ioe) {
            System.err.println("readtileset from URL failed.");
        }
        return null;
    }

    /**
     * <p>readTileset.</p>
     *
     * @param filename a {@link String} object.
     * @return a {@link org.mapeditor.core.TileSet} object.
     * @throws IOException if any.
     */
    public TileSet readTileset(String filename) throws IOException {
        String xmlFile = makeUrl(filename);
        URL url = new URL(xmlFile);
        return readTileset(url.openStream());
    }

    private Map buildMap(Map map, String xmlPath) throws IOException {
        List<TileSet> tilesets = map.getTileSets();
        for (int i = 0; i < tilesets.size(); i++) {
            TileSet tileset = tilesets.get(i);
            String tileSetSource = tileset.getSource();
            if (tileSetSource != null) {
                int firstGid = tileset.getFirstgid();
                tileset = readTileset(xmlPath + tileSetSource);
                tileset.setFirstgid(firstGid);
                tileset.setSource(tileSetSource);
                tilesets.set(i, tileset);
            }
        }
        return map;
    }

    /**
     * Builds a game map that contains all of the pertinent information using web sources.
     * This should not be called if not using web URLs for parsing.
     * @param map The map object that is unmarshalled and should be further built.
     * @return The fully built object.
     * @throws IOException if any, especially if links cannot be followed.
     *
     * @author Jacob Ekstrum
     */
    private Map buildMap(Map map) throws IOException {
        List<TileSet> tilesets = map.getTileSets();
        for (int i = 0; i < tilesets.size(); i++) {
            TileSet tileset = tilesets.get(i);
            String tileSetSource = tileset.getSource();
            if (tileSetSource != null) {
                URL sourceURL = new URL(tileSetSource);
                int firstGid = tileset.getFirstgid();
                tileset = readTileset(sourceURL);
                tileset.setFirstgid(firstGid);
                tileset.setSource(tileSetSource);
                tilesets.set(i, tileset);
            }
        }
        return map;
    }

    /**
     * I don't think this is used anymore, and I also think it's broken.
     *
     * @author Jacob Ekstrum
     *
     * @param map
     * @param resourceLoader
     * @return
     * @throws IOException
     */
    private Map buildMap(Map map, Class resourceLoader) throws IOException {
        List<TileSet> tilesets = map.getTileSets();
        for (int i = 0; i < tilesets.size(); i++) {
            TileSet tileset = tilesets.get(i);
            String tileSetSource = tileset.getSource();
            if (tileSetSource != null) {
                int firstGid = tileset.getFirstgid();
                InputStream is = resourceLoader.getResourceAsStream(tileSetSource);
                tileset = readTileset(is);
                tileset.setFirstgid(firstGid);
//                tileset.setSource(tileSetSource);
                tilesets.set(i, tileset);
            }
        }
        return map;
    }

    private String makeUrl(String filename) {
        final String url;
        if (filename.indexOf("://") > 0 || filename.startsWith("file:")) {
            url = filename;
        } else {
            url = new File(filename).toURI().toString();
        }
        return url;
    }

    /**
     * Performs unmarshalling using a web URL to gather the data for the input stream.
     * @param in The URL to be used.
     * @param type The type to be fit to the unmarshalling.
     * @param <T> The type of the class.
     * @return The unmarshalled object.
     */
    private <T> T unmarshal(URL in, Class<T> type) {
        try {
            InputStream inp = in.openStream();
            return unmarshal(inp, type);
        } catch (IOException ex) {
            Logger.getLogger(org.mapeditor.io.MapReader.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private <T> T unmarshal(InputStream in, Class<T> type) {
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader reader = factory.createXMLEventReader(in);

            JAXBContext context = JAXBContext.newInstance(type);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            JAXBElement<T> element = unmarshaller.unmarshal(reader, type);
            return element.getValue();
        } catch (XMLStreamException | JAXBException ex) {
            Logger.getLogger(org.mapeditor.io.MapReader.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
