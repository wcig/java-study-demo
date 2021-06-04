package com.wcig.app.gm;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.core.ImageCommand;

public class Main {
    public static void main(String[] args) throws Exception {
        System.setProperty("im4java.useGM", "true");

        String src = "img/src.jpg";
        scaleResizeV1(src, "img/out.600x600.v1.jpg", 600, 600);
        scaleResizeV2(src, "img/out.600x600.v2.jpg", 600, 600);
        scaleResizeV3(src, "img/out.600x600.v3.jpg", 600, 600);

        scaleResizeV1(src, "img/out.5000x5000.v1.jpg", 5000, 5000);
        scaleResizeV2(src, "img/out.5000x5000.v2.jpg", 5000, 5000);
        scaleResizeV3(src, "img/out.5000x5000.v3.jpg", 5000, 5000);
        // output:
        // gm convert img/src.jpg -sample 600x600 img/out.600x600.v1.jpg
        // gm convert img/src.jpg -resize 600x600^ -gravity center -extent 600x600 img/out.600x600.v2.jpg
        // gm convert img/src.jpg -resize 600x600> -gravity center -extent 600x600 img/out.600x600.v3.jpg
        // gm convert img/src.jpg -sample 5000x5000 img/out.5000x5000.v1.jpg
        // gm convert img/src.jpg -resize 5000x5000^ -gravity center -extent 5000x5000 img/out.5000x5000.v2.jpg
        // gm convert img/src.jpg -resize 5000x5000> -gravity center -extent 5000x5000 img/out.5000x5000.v3.jpg
    }

    public static void scaleResizeV1(String srcImgPath, String destImgPath, Integer width, Integer height) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImgPath);
        op.sample(width, height);
        op.addImage(destImgPath);
        ImageCommand cmd = new ConvertCmd();
        printCmd(cmd, op);
        cmd.run(op);
    }

    public static void scaleResizeV2(String srcImgPath, String destImgPath, Integer width, Integer height) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImgPath);
        op.resize(width, height, "^");
        op.gravity("center").extent(width, height);
        op.addImage(destImgPath);
        ImageCommand cmd = new ConvertCmd(true);
        printCmd(cmd, op);
        cmd.run(op);
    }

    public static void scaleResizeV3(String srcImgPath, String destImgPath, Integer width, Integer height) throws Exception {
        IMOperation op = new IMOperation();
        op.addImage(srcImgPath);
        op.resize(width, height, ">");
        op.gravity("center").extent(width, height);
        op.addImage(destImgPath);
        ImageCommand cmd = new ConvertCmd(true);
        printCmd(cmd, op);
        cmd.run(op);
    }

    public static void printCmd(ImageCommand cmd, IMOperation op) {
        final String val = String.join(" ", cmd.getCommand());
        System.out.println(val + " " + op);
    }
}
