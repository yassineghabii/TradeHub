package com.example.pifinance_back.Entities;

import net.coobird.thumbnailator.Thumbnails;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageProcessor {

    // Méthode pour redimensionner une image à une taille spécifique
    public byte[] resizeImage(InputStream input, int width, int height) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Thumbnails.of(input)
                .size(width, height)
                .outputFormat("jpg") // Vous pouvez spécifier le format souhaité ici
                .outputQuality(0.5) // Réglez la qualité de sortie
                .toOutputStream(outputStream);

        return outputStream.toByteArray();
    }
}
