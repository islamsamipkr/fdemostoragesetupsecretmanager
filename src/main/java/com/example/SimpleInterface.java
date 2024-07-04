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
import java.io.IOException;
import java.nio.file.Files;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.secretmanager.v1.AccessSecretVersionRequest;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import com.google.cloud.secretmanager.v1.SecretPayload;

public class SimpleInterface {

    public static void main(String[] args) {
        // Create a new frame
        JFrame frame = new JFrame("Simple Interface");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a panel with BoxLayout for vertical alignment
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Create a label
        JLabel label = new JLabel("This is a test for moving reference from SMB Mount to Cloud Storage", JLabel.CENTER);
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        // Create a button for file upload
        JButton uploadButton = new JButton("Upload File");
        uploadButton.setAlignmentX(JButton.CENTER_ALIGNMENT);

        // Add action listener to the upload button
        uploadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Create a file chooser
                JFileChooser fileChooser = new JFileChooser();
                
                // Show the open dialog
                int result = fileChooser.showOpenDialog(frame);

                // If a file is selected
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    // Upload the file to Google Cloud Storage
                    try {
                        uploadToGCS(selectedFile);
                        // Show a message dialog with the selected file's path
                        JOptionPane.showMessageDialog(frame, "File Uploaded: " + selectedFile.getAbsolutePath());
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "File Upload Failed: " + ex.getMessage());
                    }
                }
            }
        });

        // Add the label and the upload button to the panel
        panel.add(label);
        panel.add(uploadButton);

        // Add the panel to the frame
        frame.add(panel);

        // Set the frame visibility to true
        frame.setVisible(true);
    }

    private static void uploadToGCS(File file) throws IOException {
        // Set up Google Cloud Storage
        String bucketName = "democloudstoragebucketupload"; // Change this to your bucket name

        // Fetch credentials from Google Cloud Secret Manager
        Credentials credentials = getCredentialsFromSecretManager();

        // Get the Storage instance with the retrieved credentials
        Storage storage = StorageOptions.newBuilder()
                                        .setCredentials(credentials)
                                        .build()
                                        .getService();

        // Create a BlobId and BlobInfo
        BlobId blobId = BlobId.of(bucketName, file.getName());
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        // Upload the file
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
    }

    private static Credentials getCredentialsFromSecretManager() throws IOException {
        // Replace with your secret ID and secret version
        String secretId = "demostoragekey";
        String secretVersion = "latest";

        // Create Secret Manager client
        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            // Build the request
            AccessSecretVersionRequest request = AccessSecretVersionRequest.newBuilder()
                .setName(secretId + "/versions/" + secretVersion)
                .build();

            // Access the secret
            SecretPayload payload = client.accessSecretVersion(request).getPayload();

            // Parse the JSON credentials and return as GoogleCredentials
            return GoogleCredentials.fromStream(payload.getData().newInput());
        }
    }
}
