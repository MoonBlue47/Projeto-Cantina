package com.senai.projetoCantina.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.EnumMap;
import java.util.Map;

/**
 * Servico responsavel por gerar o payload Pix EMV (Copia e Cola estatico)
 * conforme especificacao do Banco Central do Brasil (BR Code / PIX QR Code).
 */
@Service
public class PixService {

    // Dados do recebedor
    private static final String CHAVE_PIX      = "53923712804";
    private static final String NOME_RECEBEDOR = "Matheus Brizzi";
    private static final String CIDADE         = "Sao Paulo";

    // Identificadores de campo EMV (BR Code)
    private static final String ID_PAYLOAD_FORMAT      = "00";
    private static final String ID_POINT_OF_INITIATION = "01";
    private static final String ID_MERCHANT_ACCOUNT    = "26";
    private static final String ID_MERCHANT_CAT        = "52";
    private static final String ID_CURRENCY            = "53";
    private static final String ID_AMOUNT              = "54";
    private static final String ID_COUNTRY             = "58";
    private static final String ID_MERCHANT_NAME       = "59";
    private static final String ID_MERCHANT_CITY       = "60";
    private static final String ID_ADDITIONAL_DATA     = "62";
    private static final String ID_CRC16               = "63";

    // Sub-campos
    private static final String ID_GUI   = "00";
    private static final String ID_CHAVE = "01";
    private static final String ID_TXID  = "05";

    // Valores fixos
    private static final String PAYLOAD_FORMAT_VALUE = "01";
    private static final String GUI_VALUE            = "BR.GOV.BCB.PIX";
    private static final String CURRENCY_BRL         = "986";
    private static final String COUNTRY_BR           = "BR";
    private static final String TXID_PLACEHOLDER     = "***";

    /**
     * Gera a string EMV (Pix Copia e Cola) estatica para o valor informado.
     */
    public String gerarPixCopiaCola(BigDecimal valor) {
        if (valor == null) {
            valor = BigDecimal.ZERO;
        }
        String merchantAccount = buildTLV(ID_GUI, GUI_VALUE)
                + buildTLV(ID_CHAVE, CHAVE_PIX);
        String additionalData  = buildTLV(ID_TXID, TXID_PLACEHOLDER);
        String nomeFormatado   = truncar(NOME_RECEBEDOR, 25);
        String cidadeFormatada = truncar(CIDADE, 15);
        String valorFormatado  = formatarValor(valor);

        StringBuilder payload = new StringBuilder();
        payload.append(buildTLV(ID_PAYLOAD_FORMAT,      PAYLOAD_FORMAT_VALUE));
        payload.append(buildTLV(ID_POINT_OF_INITIATION, "12"));
        payload.append(buildTLV(ID_MERCHANT_ACCOUNT,    merchantAccount));
        payload.append(buildTLV(ID_MERCHANT_CAT,        "0000"));
        payload.append(buildTLV(ID_CURRENCY,            CURRENCY_BRL));
        payload.append(buildTLV(ID_AMOUNT,              valorFormatado));
        payload.append(buildTLV(ID_COUNTRY,             COUNTRY_BR));
        payload.append(buildTLV(ID_MERCHANT_NAME,       nomeFormatado));
        payload.append(buildTLV(ID_MERCHANT_CITY,       cidadeFormatada));
        payload.append(buildTLV(ID_ADDITIONAL_DATA,     additionalData));
        payload.append(ID_CRC16).append("04");
        payload.append(calcularCRC16(payload.toString()));

        return payload.toString();
    }

    public String gerarQrCodeBase64(String pixString) {
        try {
            Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            hints.put(EncodeHintType.MARGIN, 1);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            QRCodeWriter qrWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrWriter.encode(pixString, BarcodeFormat.QR_CODE, 300, 300, hints);

            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.setUseCache(false); // CORREÇÃO: Evita erro 500 (Can't create cache file) ao processar imagem em memória sem depender de disco
            ImageIO.write(image, "PNG", outputStream);

            return Base64.getEncoder().encodeToString(outputStream.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar QR Code Pix: " + e.getMessage(), e);
        }
    }

    // Metodos auxiliares privados

    private String buildTLV(String tag, String value) {
        return tag + String.format(java.util.Locale.US, "%02d", value.length()) + value;
    }

    private String formatarValor(BigDecimal valor) {
        return String.format(java.util.Locale.US, "%.2f", valor);
    }

    private String truncar(String texto, int maxLength) {
        if (texto == null) return "";
        return texto.length() > maxLength ? texto.substring(0, maxLength) : texto;
    }

    /**
     * Calcula o CRC-16/CCITT-FALSE conforme exigido pelo padrao EMV BR Code.
     * Polinomio: 0x1021 | Valor inicial: 0xFFFF | Sem reflexao.
     */
    private String calcularCRC16(String payload) {
        int crc = 0xFFFF;
        byte[] bytes = payload.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        for (byte b : bytes) {
            crc ^= (b & 0xFF) << 8;
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x8000) != 0) {
                    crc = (crc << 1) ^ 0x1021;
                } else {
                    crc <<= 1;
                }
                crc &= 0xFFFF;
            }
        }
        return String.format("%04X", crc);
    }
}