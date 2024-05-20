package com.SAMProject.BillingSystem.util;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

import com.SAMProject.BillingSystem.entity.StatusDetails;
import com.SAMProject.BillingSystem.entity.User;
import com.SAMProject.BillingSystem.service.BillingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvoiceWriter {

    private static final Logger log = LoggerFactory.getLogger(InvoiceWriter.class);


    public static String generateInvoice(StatusDetails statusDetails) {
        User user = statusDetails.getCurrentDriver();
        String invoiceDate = LocalDate.now().toString();
        String invoiceContent = "For user " + user.getUsername() + " with UserID " +
                                        user.getId() + " was a new invoice created \n" +
                                        " The Total Cost is calculcated by the provided StatusDetails from the vehicle: \n" +
                                        statusDetails.toString() +
                                        " Invoice was created on " + invoiceDate;

        return invoiceContent;
    }
    public static void saveInvoiceToFile(String invoiceContent, StatusDetails statusDetails) {
        File directory = new File("BillingSystem/target/invoices");

        if (!directory.exists()) {
            directory.mkdirs();  // Create the directory if it does not exist
        }

        File invoiceFile = new File(directory, "invoice_User_" + statusDetails.getCurrentDriver().getId() + ".txt");

        try (FileWriter writer = new FileWriter(invoiceFile)) {
            writer.write(invoiceContent);
            log.info("Invoice saved to: {}", invoiceFile.getAbsolutePath());
        } catch (IOException e) {
            log.error("Error writing invoice to file", e);
        }
    }
}
