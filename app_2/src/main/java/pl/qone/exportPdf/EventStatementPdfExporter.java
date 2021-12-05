package pl.qone.exportPdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import pl.qone.payload.response.EventStatementResponse;

public class EventStatementPdfExporter {
	
	private static final Logger logger = LoggerFactory.getLogger(EventStatementPdfExporter.class);
	
	 public static ByteArrayInputStream eventsReportList(List<EventStatementResponse> listEvents) {

	        Document document = new Document();
	        ByteArrayOutputStream out = new ByteArrayOutputStream();

	        try {

	            PdfPTable table = new PdfPTable(5);
	            table.setWidthPercentage(85);
	            table.setWidths(new int[]{3, 1, 2, 3, 3});

	            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

	            PdfPCell hcell;
	            hcell = new PdfPCell(new Phrase("Event name", headFont));
	            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            table.addCell(hcell);

	            hcell = new PdfPCell(new Phrase("Rate", headFont));
	            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            table.addCell(hcell);

	            hcell = new PdfPCell(new Phrase("Enrolls / free space", headFont));
	            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            table.addCell(hcell);
	            
	            hcell = new PdfPCell(new Phrase("Organizer (person)", headFont));
	            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            table.addCell(hcell);
	            
	            hcell = new PdfPCell(new Phrase("Organizer (company)", headFont));
	            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            table.addCell(hcell);

	            for (EventStatementResponse ev : listEvents) {

	                PdfPCell cell;

	                cell = new PdfPCell(new Phrase(ev.getName()));
	                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	                table.addCell(cell);

	                cell = new PdfPCell(new Phrase(ev.getAverageRate().toString()));
	                cell.setPaddingLeft(5);
	                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	                table.addCell(cell);

	                cell = new PdfPCell(new Phrase(String.valueOf(ev.getContestantNumbers())));
	                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	                cell.setPaddingRight(5);
	                table.addCell(cell);
	                
	                cell = new PdfPCell(new Phrase(ev.getOrganizer()));
	                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	                cell.setPaddingRight(5);
	                table.addCell(cell);
	                
	                cell = new PdfPCell(new Phrase(ev.getCompany()));
	                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	                cell.setPaddingRight(5);
	                table.addCell(cell);
	            }

	            PdfWriter.getInstance(document, out);
	            document.open();
	            Paragraph preface = new Paragraph("Events ranking"); 
	            preface.setAlignment(Element.ALIGN_CENTER);
	            document.add(preface);
	            document.add( Chunk.NEWLINE );
	            document.add(table);

	            document.close();

	        } catch (DocumentException ex) {

	            logger.error("Error occurred: {0}", ex);
	        }

	        return new ByteArrayInputStream(out.toByteArray());
	    }
}