/*
 * Copyright (c) 2012. Brockmann Consult (info@brockmann-consult.de)
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation. This program is distributed in the hope it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA
 */

package org.esa.beam.dataio.s3.slstr;

import org.esa.beam.dataio.s3.manifest.Manifest;
import org.esa.beam.framework.dataio.ProductReaderPlugIn;

import java.util.ArrayList;
import java.util.List;

/**
 * Product reader responsible for reading SLSTR SST WCT data products in SAFE format.
 *
 * @author Olaf Danne
 * @author Ralf Quast
 * @since 1.0
 */
class SlstrSstProductReader extends SlstrProductReader {

    SlstrSstProductReader(ProductReaderPlugIn readerPlugIn) {
        super(readerPlugIn);
    }

    @Override
    protected List<String> getFileNames(Manifest manifest) {
        final List<String> fileNames = new ArrayList<String>();

        fileNames.addAll(manifest.getFileNames("LSTdataSchema"));
        if (!fileNames.isEmpty()) {
            return fileNames;
        }

        fileNames.addAll(manifest.getFileNames("N2_SST_schema"));
        fileNames.addAll(manifest.getFileNames("N3R_SST_schema"));
        fileNames.addAll(manifest.getFileNames("N3_SST_schema"));

        fileNames.addAll(manifest.getFileNames("geodeticTiepointCoordinatesSchema"));
        fileNames.addAll(manifest.getFileNames("cartesianTiepointCoordinatesSchema"));
        fileNames.addAll(manifest.getFileNames("nadirSolarViewGeometrySchema"));
        fileNames.addAll(manifest.getFileNames("meteorologicalDataSchema"));

        fileNames.addAll(manifest.getFileNames("nadirFlagsSchema"));
        fileNames.addAll(manifest.getFileNames("nadirIndicesSchema"));

        fileNames.addAll(manifest.getFileNames("D2_SST_schema"));
        fileNames.addAll(manifest.getFileNames("D3R_SST_schema"));
        fileNames.addAll(manifest.getFileNames("D3_SST_schema"));

        fileNames.addAll(manifest.getFileNames("obliqueFlagsSchema"));
        fileNames.addAll(manifest.getFileNames("obliqueIndicesSchema"));

        // TODO - not clear how to represent cartesian data
        // TODO - not clear how to represent time data

        return fileNames;
    }
}