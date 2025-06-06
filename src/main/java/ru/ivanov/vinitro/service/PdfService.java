package ru.ivanov.vinitro.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;
import ru.ivanov.vinitro.model.AnalysisResult;
import ru.ivanov.vinitro.model.AppointmentForAnalysis;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class PdfService {

    public byte[] generateAppointmentPdf(AppointmentForAnalysis appointment) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDType0Font font = PDType0Font.load(document, getClass().getResourceAsStream("/fonts/DejaVuSans.ttf"));

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float margin = 50;
                float yStart = 750;
                float leading = 15f;

                contentStream.setFont(font, 12);

                // Основные данные
                writeLine(contentStream, margin, yStart -= leading, "Анализ: " + appointment.getAnalysis().getName());
                writeLine(contentStream, margin, yStart -= leading, "Пациент: " + appointment.getPatient().getInitials());
                writeLine(contentStream, margin, yStart -= leading, "Дата: " + appointment.getDate());
                writeLine(contentStream, margin, yStart -= leading, "Время: " + appointment.getTime());

                writeLine(contentStream, margin, yStart -= leading * 2, "Результаты:");

                for (AnalysisResult result : appointment.getResults()) {
                    for (Map.Entry<String, Object> entry : result.getValues().entrySet()) {
                        String displayValue = convertValueToString(entry.getValue());
                        writeLine(contentStream, margin + 20, yStart -= leading,
                                entry.getKey() + ": " + displayValue);
                    }
                }
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            return outputStream.toByteArray();
        }
    }

    private void writeLine(PDPageContentStream cs, float x, float y, String text) throws IOException {
        if (y < 50) {
            throw new IOException("Out of space on PDF page");
        }
        cs.beginText();
        cs.newLineAtOffset(x, y);
        cs.showText(text);
        cs.endText();
    }

    private String convertValueToString(Object value) {
        if (value instanceof Boolean) {
            return (Boolean) value ? "Да" : "Нет";
        }

        if (value instanceof List<?>) {
            List<?> list = (List<?>) value;

            if (!list.isEmpty()) {
                Object first = list.get(0);

                if (first instanceof Boolean) {
                    return (Boolean) first ? "Да" : "Нет";
                }
                return first != null ? first.toString() : "";
            }
        }
        return value != null ? value.toString() : "";
    }
}

/*
    private String transliterate(String text) {
        Map<Character, String> map = new HashMap<>();
        map.put('а', "a");
        map.put('б', "b");
        map.put('в', "v");
        map.put('г', "g");
        map.put('д', "d");
        map.put('е', "e");
        map.put('ё', "e");
        map.put('ж', "zh");
        map.put('з', "z");
        map.put('и', "i");
        map.put('й', "y");
        map.put('к', "k");
        map.put('л', "l");
        map.put('м', "m");
        map.put('н', "n");
        map.put('о', "o");
        map.put('п', "p");
        map.put('р', "r");
        map.put('с', "s");
        map.put('т', "t");
        map.put('у', "u");
        map.put('ф', "f");
        map.put('х', "h");
        map.put('ц', "ts");
        map.put('ч', "ch");
        map.put('ш', "sh");
        map.put('щ', "shch");
        map.put('ъ', "");
        map.put('ы', "y");
        map.put('ь', "");
        map.put('э', "e");
        map.put('ю', "yu");
        map.put('я', "ya");

        // То же для заглавных букв
        map.put('А', "A");
        map.put('Б', "B");
        map.put('В', "V");
        map.put('Г', "G");
        map.put('Д', "D");
        map.put('Е', "E");
        map.put('Ё', "E");
        map.put('Ж', "Zh");
        map.put('З', "Z");
        map.put('И', "I");
        map.put('Й', "Y");
        map.put('К', "K");
        map.put('Л', "L");
        map.put('М', "M");
        map.put('Н', "N");
        map.put('О', "O");
        map.put('П', "P");
        map.put('Р', "R");
        map.put('С', "S");
        map.put('Т', "T");
        map.put('У', "U");
        map.put('Ф', "F");
        map.put('Х', "H");
        map.put('Ц', "Ts");
        map.put('Ч', "Ch");
        map.put('Ш', "Sh");
        map.put('Щ', "Shch");
        map.put('Ъ', "");
        map.put('Ы', "Y");
        map.put('Ь', "");
        map.put('Э', "E");
        map.put('Ю', "Yu");
        map.put('Я', "Ya");

        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            sb.append(map.getOrDefault(c, String.valueOf(c)));
        }
        return sb.toString();
    }
 */
