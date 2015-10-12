package com.issuudownloader;

import java.io.IOException;
import java.util.Scanner;

public class Main {



    public static void main(final String[] args){
        String url = "";
        if(args.length==0){
            Scanner sc = new Scanner(System.in);
            System.out.println("URL of the ISSUU publication :");
            url = sc.nextLine();

        } else if(args.length==1){
            url = args[0];
        } else {
            System.err.println("Error : too many arguments");
            System.exit(-1);
        }

        ISSUUdownloader issuUdownloader = new ISSUUdownloader(url);
        try {
            issuUdownloader.download();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
