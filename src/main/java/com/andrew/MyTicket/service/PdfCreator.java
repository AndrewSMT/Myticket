package com.andrew.MyTicket.service;

import com.andrew.MyTicket.model.Cart;
import com.andrew.MyTicket.model.Ticket;
import com.andrew.MyTicket.repositories.TicketRepo;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

@Service
public class PdfCreator {
    @Autowired
    private TicketRepo ticketRepo;
    public Document createPdfOrder(Cart userCart, String fileName) throws IOException, DocumentException {
        Set<Ticket> tickets = userCart.getTicket();
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter writer = PdfWriter.getInstance(document,
        new FileOutputStream("C:\\Users\\Andrew\\IdeaProjects\\NewProjects\\MyTicket\\src\\main\\resources\\tempPdf\\"+fileName+".pdf"));
        document.open();
        document.add(new Paragraph("№ Order: "+ tickets.stream().findFirst().get().getOrderNumber()));
        document.add(new Paragraph("............................................................................................................................................."));
        for(Ticket tc: tickets){
                document.add(new Paragraph("Ticket"));
                document.add(new Paragraph("Row: " + tc.getRow() + ", Number: " + tc.getRow() + tc.getNumber() + ", Event:" + tc.getEvent().getTitle() + ", Date: " + tc.getEvent().getDate() + ", Place: " + tc.getEvent().getPlace().getTitle() + ", Price: " + tc.getPrice() + " UAH"));
                document.add(new Paragraph("_________________________________________________________________________"));
            }
        document.close();
        return document;
    }
}
