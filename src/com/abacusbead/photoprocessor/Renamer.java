package com.abacusbead.photoprocessor;

import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegImageParser;
import org.apache.sanselan.formats.tiff.constants.ExifTagConstants;
import org.apache.sanselan.formats.tiff.TiffField;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.ImageReadException;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import com.abacusbead.utils.ArgParser;

/**
 * @author: mizhang
 * @since: Dec 11, 2014 1:19:07 PM
 */
public class Renamer {

    final private static String DEFAULT_OUTOUTFORMATSTRING;
    final private static SimpleDateFormat exifInputFormat;

    static {
        DEFAULT_OUTOUTFORMATSTRING = "yyyy.MM.dd_HH.mm.ss";
        exifInputFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");

//        exifInputFormat.setTimeZone(TimeZone.getTimeZone("US/Eastern"));
    }

    public static boolean renameByDate(String baseDir, File file, SimpleDateFormat outputFormat) throws IOException, ImageReadException, ParseException {
        final IImageMetadata metadata = new JpegImageParser().getMetadata(file);
        final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
        final TiffField field = jpegMetadata.findEXIFValue(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);

        String modifiedDate = field.getValueDescription();
        Date date = exifInputFormat.parse(modifiedDate.substring(1, modifiedDate.length() - 1));
        File newfile = new File(baseDir + System.getProperty("file.separator") + outputFormat.format(date) + ".JPG");
        System.out.println("[Log] Rename file " + file.getAbsolutePath() + " to " + newfile.getAbsolutePath());
        return file.renameTo(newfile);
    }


    public static void renameByDate(String path, String outputFormatString) throws IOException, ImageReadException, ParseException {
        System.out.println("[Log] Start rename files in folder " + path);
        File dir = new File(path);
        if (! (dir.exists() && dir.isDirectory())) {
            System.err.println("[ERROR] " + path + " does not exist or it is a regular file");
            System.exit(1);
        }

        SimpleDateFormat outputFormat = new SimpleDateFormat(outputFormatString);
        int count = 0;
        for (File f : dir.listFiles()) {
            if(renameByDate(path, f, outputFormat)) {
                count++;
            }
        }

        System.out.println("[Log] Totally renamed " + count + " files");
    }

    public static void main(String[] args) throws IOException, ImageReadException, ParseException {
/*
        if (args.length == 0) {
            System.out.println("USAGE:\ncom.abacusbead.photoprocessor.Renamer [--outputformat OUTPUTFORMAT] --dir INPUT_DIR");
            System.exit(1);
        }

        ArgParser argParser = new ArgParser(args);

        if (!argParser.hasArg("dir")) {
            System.out.println("You must specify the input dir\nUSAGE:\ncom.abacusbead.photoprocessor.Renamer [--outputformat OUTPUTFORMAT] --dir INPUT_DIR");
            System.exit(1);

        }

        String outputFormatString = argParser.hasArg("outputformat") ? argParser.getValue("outputformat"): DEFAULT_OUTOUTFORMATSTRING;
        renameByDate(argParser.getValue("dir"), outputFormatString);
*/
        if (args.length == 0) {
            System.out.println("USAGE:\ncom.abacusbead.photoprocessor.Renamer INPUT_DIR [OUTPUTFORMAT]");
            System.exit(1);
        }

        String outputFormatString = args.length == 1 ? DEFAULT_OUTOUTFORMATSTRING : args[1];
        renameByDate(args[0], outputFormatString);
    }
}

