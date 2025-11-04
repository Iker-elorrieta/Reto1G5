package modelo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

public class GestorBackup {

    public void leerDat() {
        String defaultPath = "RETOSEGUNDO/backup_global.dat";
        File f = new File(defaultPath);
        if (!f.exists()) defaultPath = "backup_global.dat";
        try {
            leerDat(defaultPath);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error leyendo .dat: " + e.getMessage());
        }
    }

    public void leerDat(String ruta) throws IOException, ClassNotFoundException {
        File file = new File(ruta);
        if (!file.exists()) throw new FileNotFoundException("Archivo no encontrado: " + ruta);

        System.out.println("Leyendo archivo .dat: " + ruta + " (tamaño: " + file.length() + " bytes)");

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int read;
            while ((read = bis.read(buffer)) != -1) {
                baos.write(buffer, 0, read);
            }
            byte[] data = baos.toByteArray();
            String text = new String(data, StandardCharsets.UTF_8);

            if (isProbablyText(text)) {
                System.out.println("Contenido (texto):");
                System.out.println(text);
                parsearContenido(text);
            } else {
                System.out.println("Intentando deserializar objeto...");
                try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
                    Object obj = ois.readObject();
                    System.out.println("Objeto deserializado: " + obj);
                } catch (Exception e) {
                    System.out.println("No se pudo deserializar. Mostrando bytes en hexadecimal:");
                    System.out.println(bytesToHex(data, 128));
                    String texto = extraerTextoLegible(data);
                    parsearContenido(texto);
                }
            }
        }
    }

    private boolean isProbablyText(String s) {
        int control = 0;
        int len = Math.min(s.length(), 1000);
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (Character.isISOControl(c) && !Character.isWhitespace(c)) control++;
        }
        return control < 5;
    }

    private String bytesToHex(byte[] bytes, int maxBytes) {
        StringBuilder sb = new StringBuilder();
        Formatter fmt = new Formatter(sb);
        int limit = Math.min(bytes.length, maxBytes);
        for (int i = 0; i < limit; i++) {
            fmt.format("%02X ", bytes[i]);
            if ((i + 1) % 16 == 0) sb.append('\n');
        }
        fmt.close();
        if (bytes.length > limit)
            sb.append("\n... (").append(bytes.length - limit).append(" more bytes)");
        return sb.toString();
    }

    private String extraerTextoLegible(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append((b >= 32 && b <= 126) ? (char) b : ' ');
        }
        String texto = sb.toString().replaceAll(" +", " ").trim();
        System.out.println("Texto extraído:");
        System.out.println(texto);
        return texto;
    }

    private void parsearContenido(String texto) {
        String[] partes = texto.split(" ");
        List<String> fechas = new ArrayList<>();
        List<String> urls = new ArrayList<>();
        List<String> rutinas = new ArrayList<>();
        List<String> emails = new ArrayList<>();

        for (int i = 0; i < partes.length; i++) {
            String p = partes[i];
            if (p.matches("[A-Z][a-z]{2} [A-Z][a-z]{2} \\d{2} \\d{2}:\\d{2}:\\d{2} [A-Z]{3,4} \\d{4}")) {
                fechas.add(p + " " + partes[i + 1] + " " + partes[i + 2]);
            } else if (p.contains("@")) {
                emails.add(p);
            } else if (p.startsWith("http") || p.startsWith("+http")) {
                urls.add(p.replace("+", ""));
                if (i > 0) rutinas.add(partes[i - 1]);
            }
        }

        System.out.println("\n=== Emails detectados ===");
        emails.forEach(e -> System.out.println("📧 " + e));

        System.out.println("\n=== Fechas detectadas ===");
        fechas.forEach(f -> System.out.println("📅 " + f));

        System.out.println("\n=== Rutinas detectadas ===");
        for (int i = 0; i < rutinas.size(); i++) {
            System.out.println("🏋️ " + rutinas.get(i) + " → " + urls.get(i));
        }
    }

    public void leerXml() {
        String defaultPath = "RETOSEGUNDO/historico_global.xml";
        File f = new File(defaultPath);
        if (!f.exists()) defaultPath = "historico_global.xml";
        try {
            leerXml(defaultPath);
        } catch (Exception e) {
            System.err.println("Error leyendo .xml: " + e.getMessage());
        }
    }

    public void leerXml(String ruta) throws Exception {
        File xmlFile = new File(ruta);
        if (!xmlFile.exists()) throw new FileNotFoundException("Archivo no encontrado: " + ruta);

        System.out.println("Leyendo archivo .xml: " + ruta);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setIgnoringComments(true);
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(xmlFile);
        doc.getDocumentElement().normalize();
        Element root = doc.getDocumentElement();
        System.out.println("Elemento raíz: " + root.getNodeName());
        printElement(root, 0);
    }

    private void printElement(Element el, int indent) {
        String pad = "  ".repeat(indent);
        System.out.print(pad + "<" + el.getNodeName());

        NamedNodeMap attrs = el.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            Node attr = attrs.item(i);
            System.out.print(" " + attr.getNodeName() + "=\"" + attr.getNodeValue() + "\"");
        }
        System.out.println(">");

        NodeList children = el.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                printElement((Element) n, indent + 1);
            } else if (n.getNodeType() == Node.TEXT_NODE) {
                String text = n.getTextContent().trim();
                if (!text.isEmpty()) {
                    System.out.println(pad + "  " + text);
                }
            }
        }
        System.out.println(pad + "</" + el.getNodeName() + ">");
    }
}
