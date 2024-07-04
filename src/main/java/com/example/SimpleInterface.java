package com.example;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public class SimpleInterface {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Simple Interface");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("This is a test for moving reference from SMB Mount to Cloud Storage", JLabel.CENTER);
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        JButton uploadButton = new JButton("Upload File");
        uploadButton.setAlignmentX(JButton.CENTER_ALIGNMENT);

        uploadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                JFileChooser fileChooser = new JFileChooser();
                
                int result = fileChooser.showOpenDialog(frame);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    // Upload the file to Google Cloud Storage
                    try {
                        uploadToGCS(selectedFile);
                        JOptionPane.showMessageDialog(frame, "File Uploaded: " + selectedFile.getAbsolutePath());
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "File Upload Failed: " + ex.getMessage());
                    }
                }
            }
        });


        panel.add(label);
        panel.add(uploadButton);

        frame.add(panel);

        frame.setVisible(true);
    }

    private static void uploadToGCS(File file) throws IOException {
        // Set up Google Cloud Storage
        String bucketName = "democloudstoragebucketupload"; // this is the name of the bucket replacing the smb mount
        String blobName = file.getName();

        // Get the Storage instance
        Storage storage = StorageOptions.getDefaultInstance().getService();

        // Create a BlobId and BlobInfo
        BlobId blobId = BlobId.of(bucketName, blobName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        // Upload the file
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
    }
}
