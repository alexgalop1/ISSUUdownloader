package com.issuudownloader;

import java.io.IOException;

/**
 * Created by thesealex on 12/10/15.
 */
public class Main {



    public static void main(final String[] args){
        if(args.length!=1){
            System.err.println("Error : ISSUU publication is missing");
            System.exit(-1);
        }

        String url = args[0];
        ISSUUdownloader issuUdownloader = new ISSUUdownloader(url);
        try {
            issuUdownloader.download();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
