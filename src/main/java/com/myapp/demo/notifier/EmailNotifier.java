package com.myapp.demo.notifier;

import com.myapp.demo.entity.InventoryStock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Email-based notifier that sends low-stock alerts via email
 * Requires SMTP configuration in application.properties
 */
@Component
public class EmailNotifier implements Notifier {

    private static final Logger logger = LoggerFactory.getLogger(EmailNotifier.class);

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Override
    public void notify(InventoryStock item) {
        if (mailSender == null) {
            logger.debug("JavaMailSender not configured, skipping email notification");
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom("sravanreddy254@gmail.com");
            helper.setTo("22r21a05q4@mlrit.ac.in");
            helper.setSubject("🚨 LOW STOCK ALERT: " + item.getProductName());
            helper.setText(buildHtmlEmailContent(item), true); // true = HTML
            
            mailSender.send(message);
            logger.info("Email notification sent for product: {}", item.getProductName());
        } catch (MessagingException e) {
            logger.error("Failed to send email notification for product: {}", item.getProductName(), e);
        }
    }

    private String buildHtmlEmailContent(InventoryStock item) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = LocalDateTime.now().format(formatter);
        
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <style>\n" +
                "        body { font-family: 'Arial', sans-serif; background-color: #f5f5f5; }\n" +
                "        .container { max-width: 600px; margin: 20px auto; background-color: white; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }\n" +
                "        .header { background: linear-gradient(135deg, #dc2626 0%, #991b1b 100%); color: white; padding: 30px; text-align: center; border-radius: 8px 8px 0 0; }\n" +
                "        .header h1 { margin: 0; font-size: 28px; font-weight: bold; }\n" +
                "        .header p { margin: 5px 0 0 0; font-size: 14px; opacity: 0.9; }\n" +
                "        .alert-badge { display: inline-block; background-color: #fca5a5; color: #7f1d1d; padding: 8px 16px; border-radius: 20px; font-weight: bold; margin: 10px 0; }\n" +
                "        .content { padding: 30px; }\n" +
                "        .info-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; margin: 20px 0; }\n" +
                "        .info-box { background-color: #f9fafb; padding: 15px; border-left: 4px solid #dc2626; border-radius: 4px; }\n" +
                "        .info-label { color: #6b7280; font-size: 12px; font-weight: bold; text-transform: uppercase; letter-spacing: 0.5px; }\n" +
                "        .info-value { color: #1f2937; font-size: 18px; font-weight: bold; margin-top: 5px; }\n" +
                "        .status-critical { color: #dc2626; font-weight: bold; }\n" +
                "        .status-warning { color: #f59e0b; font-weight: bold; }\n" +
                "        .action-box { background-color: #fef2f2; border: 2px dashed #fca5a5; border-radius: 6px; padding: 15px; margin: 20px 0; text-align: center; }\n" +
                "        .action-text { color: #991b1b; font-weight: bold; font-size: 14px; }\n" +
                "        .footer { background-color: #f3f4f6; padding: 20px; text-align: center; border-radius: 0 0 8px 8px; border-top: 1px solid #e5e7eb; }\n" +
                "        .footer p { margin: 5px 0; font-size: 12px; color: #6b7280; }\n" +
                "        .highlight { background-color: #fef3c7; padding: 2px 6px; border-radius: 3px; font-weight: bold; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"header\">\n" +
                "            <h1>🚨 INVENTORY ALERT</h1>\n" +
                "            <p>Low Stock Warning - Immediate Action Required</p>\n" +
                "        </div>\n" +
                "        \n" +
                "        <div class=\"content\">\n" +
                "            <div class=\"alert-badge\">⚠️ CRITICAL - Stock Below Threshold</div>\n" +
                "            \n" +
                "            <p style=\"color: #374151; font-size: 16px; line-height: 1.6;\">\n" +
                "                Dear Inventory Manager,<br><br>\n" +
                "                <span class=\"status-critical\">A critical low-stock alert has been triggered</span> for one of your products. " +
                "Please review the details below and take immediate action to restock the item.\n" +
                "            </p>\n" +
                "            \n" +
                "            <div class=\"info-grid\">\n" +
                "                <div class=\"info-box\">\n" +
                "                    <div class=\"info-label\">📦 Product Name</div>\n" +
                "                    <div class=\"info-value\" style=\"color: #1f2937;\">" + item.getProductName() + "</div>\n" +
                "                </div>\n" +
                "                \n" +
                "                <div class=\"info-box\">\n" +
                "                    <div class=\"info-label\">🆔 Product ID</div>\n" +
                "                    <div class=\"info-value\" style=\"color: #6366f1;\">#" + item.getProductId() + "</div>\n" +
                "                </div>\n" +
                "                \n" +
                "                <div class=\"info-box\">\n" +
                "                    <div class=\"info-label\">📊 Current Quantity</div>\n" +
                "                    <div class=\"info-value status-critical\">" + item.getQuantity() + " units</div>\n" +
                "                </div>\n" +
                "                \n" +
                "                <div class=\"info-box\">\n" +
                "                    <div class=\"info-label\">⚠️ Stock Threshold</div>\n" +
                "                    <div class=\"info-value status-warning\">2 units (minimum)</div>\n" +
                "                </div>\n" +
                "                \n" +
                "                <div class=\"info-box\">\n" +
                "                    <div class=\"info-label\">💰 Price Per Unit</div>\n" +
                "                    <div class=\"info-value\" style=\"color: #059669;\">₹" + item.getPricePerUnit() + "</div>\n" +
                "                </div>\n" +
                "                \n" +
                "                <div class=\"info-box\">\n" +
                "                    <div class=\"info-label\">💵 Total Value</div>\n" +
                "                    <div class=\"info-value\" style=\"color: #d97706;\">₹" + item.getTotalPrice() + "</div>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "            \n" +
                "            <div class=\"action-box\">\n" +
                "                <div class=\"action-text\">⚡ IMMEDIATE ACTION REQUIRED</div>\n" +
                "                <p style=\"margin: 10px 0 0 0; color: #7f1d1d; font-size: 13px;\">\n" +
                "                    Please replenish stock immediately to avoid stockout\n" +
                "                </p>\n" +
                "            </div>\n" +
                "            \n" +
                "            <div style=\"background-color: #f0fdf4; border-left: 4px solid #10b981; padding: 15px; border-radius: 4px; margin: 20px 0;\">\n" +
                "                <p style=\"margin: 0; color: #047857; font-size: 13px;\">\n" +
                "                    <strong>💡 Tip:</strong> Update the quantity in the inventory system once items are restocked to clear this alert.\n" +
                "                </p>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        \n" +
                "        <div class=\"footer\">\n" +
                "            <p><strong>Alert Generated:</strong> " + timestamp + "</p>\n" +
                "            <p>This is an automated alert from your Inventory Management System</p>\n" +
                "            <p style=\"margin-top: 10px; color: #9ca3af;\">© 2025 Inventory Management System - All Rights Reserved</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }
}
