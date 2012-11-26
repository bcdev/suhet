package org.esa.beam.dataio.s3;/*
 * Copyright (C) 2012 Brockmann Consult GmbH (info@brockmann-consult.de)
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, see http://www.gnu.org/licenses/
 */

import org.esa.beam.framework.dataio.DecodeQualification;
import org.esa.beam.framework.dataio.ProductIOPlugInManager;
import org.esa.beam.framework.dataio.ProductReader;
import org.esa.beam.framework.dataio.ProductReaderPlugIn;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class Sentinel3ProductReaderPlugInTest {

    private Sentinel3ProductReaderPlugInR plugIn;

    @Before
    public void setup() {
        plugIn = new Sentinel3ProductReaderPlugInR();
    }

    @Test
    public void testIfPlugInIsLoaded() {
        final ProductIOPlugInManager ioPlugInManager = ProductIOPlugInManager.getInstance();
        final Iterator<ProductReaderPlugIn> readerPlugIns = ioPlugInManager.getReaderPlugIns("SENTINEL-3");
        assertTrue(readerPlugIns.hasNext());
        assertTrue(readerPlugIns.next() instanceof Sentinel3ProductReaderPlugInR);
    }

    @Test
    public void testDecodeQualification_OlciLevel1b() {
        String path;

        path = createManifestFilePath("OL", "1", "ERR", "");
        assertEquals(DecodeQualification.INTENDED, plugIn.getDecodeQualification(path));

        path = createManifestFilePath("OL", "1", "EFR", "");
        assertEquals(DecodeQualification.INTENDED, plugIn.getDecodeQualification(path));
    }

    @Test
    public void testDecodeQualification_OlciLevel2() {
        final String path = createManifestFilePath("OL", "2", "XXX", ".SAFE");
        assertEquals(DecodeQualification.INTENDED, plugIn.getDecodeQualification(path));
    }

    @Test
    public void testDecodeQualification_SlstrLevel1b() {
        final String path = createManifestFilePath("SL", "1", "SLT", "");
        assertEquals(DecodeQualification.INTENDED, plugIn.getDecodeQualification(path));
    }

    @Test
    public void testDecodeQualification_SlstrWct() {
        final String path = createManifestFilePath("SL", "2", "WCT", ".SAFE");
        assertEquals(DecodeQualification.INTENDED, plugIn.getDecodeQualification(path));
    }

    @Test
    public void testDecodeQualification_SlstrWst() {
        final String path = createManifestFilePath("SL", "2", "WST", ".SAFE");
        assertEquals(DecodeQualification.INTENDED, plugIn.getDecodeQualification(path));
    }

    @Test
    public void testDecodeQualification_SlstrLst() {
        final String path = createManifestFilePath("SL", "2", "LST", ".SAFE");
        assertEquals(DecodeQualification.INTENDED, plugIn.getDecodeQualification(path));
    }

    @Test
    public void testDecodeQualification_SynergyLevel2() {
        final String path = createManifestFilePath("SY", "2", "SYN", ".SAFE");
        assertEquals(DecodeQualification.INTENDED, plugIn.getDecodeQualification(path));
    }

    @Test
    public void testDecodeQualification_VgtP() {
        final String path = createManifestFilePath("SY", "2", "VGP", ".SAFE");
        assertEquals(DecodeQualification.INTENDED, plugIn.getDecodeQualification(path));
    }

    @Test
    public void testDecodeQualification_VgtS() {
        final String path = createManifestFilePath("SY", "3", "VGS", ".SAFE");
        assertEquals(DecodeQualification.INTENDED, plugIn.getDecodeQualification(path));
    }

    @Test
    public void testDecodeQualification_WithInvalidDataSource() {
        String invalidPath = createManifestFilePath("SL", "1", "XXX", "");
        assertEquals(DecodeQualification.UNABLE, plugIn.getDecodeQualification(invalidPath));
    }

    @Test
    public void testDecodeQualificationWith_WrongFile() {
        final String invalidPath = "/S3_SY_2_ERR_TTTTTTTTTTTT_instanceID_GGG_CCCC_VV/someFile.doc";
        assertEquals(DecodeQualification.UNABLE, plugIn.getDecodeQualification(invalidPath));
    }

    @Test
    public void testDecodeQualification_WithoutFile() {
        final String invalidPath = "/SY_1_ERR_TTTTTTTTTTTT_instanceID_GGG_CCCC_VV";
        assertEquals(DecodeQualification.UNABLE, plugIn.getDecodeQualification(invalidPath));
    }

    @Test
    public void testSupportedInputTypes() {
        Class[] inputTypes = plugIn.getInputTypes();
        assertEquals(2, inputTypes.length);
        assertEquals(String.class, inputTypes[0]);
        assertEquals(File.class, inputTypes[1]);
    }

    @Test
    public void testCreateReaderInstanceReturnsNewInstanceEachTime() {
        final ProductReader firstInstance = plugIn.createReaderInstance();
        assertNotNull(firstInstance);
        final ProductReader secondInstance = plugIn.createReaderInstance();
        assertNotSame(secondInstance, firstInstance);
    }

    private static String createManifestFilePath(String sensorId, String levelId, String productId, String suffix) {
        String validParentDirectory = String.format("/S3_%s_%s_%s_TTTTTTTTTTTT_instanceID_GGG_CCCC_VV%s/", sensorId,
                                                    levelId, productId, suffix);
        String manifestFile = "manifest.xml";
        return validParentDirectory + manifestFile;
    }

}