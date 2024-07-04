# Step-by-Step Guide on Replacing SMB Mount Code with Cloud Storage API

## Step 1: Set Up Google Cloud Project

### Create a Google Cloud Project:
1. Go to the [Google Cloud Console](https://console.cloud.google.com/).
2. Create a new project or select an existing project.

### Enable Google Cloud Storage API:
1. Navigate to `API & Services` > `Library`.
2. Search for "Google Cloud Storage API" and enable it.

### Create a Service Account:
1. Navigate to `IAM & Admin` > `Service Accounts`.
2. Click "Create Service Account".
3. Provide a name and description, then click "Create".
4. Grant the `Storage Admin` role to the service account and click "Continue".
5. Click "Done".

### Generate Service Account Key:
1. Click on the newly created service account.
2. Go to the "Keys" tab and click `Add Key` > `Create new key`.
3. Choose JSON and click "Create".
4. Save the JSON file securely; you will need it for authentication.

### Provide the `roles/storage.admin` Permission to the Service Account:
Ensure that the service account has the `roles/storage.admin` permission.

## Step 2: Set Up Environment Variable

### Open PowerShell:
Open Windows PowerShell.

### Set Environment Variable:
```powershell
$env:GOOGLE_APPLICATION_CREDENTIALS="C:\path\to\your\service-account-file.json"
```
### Verify Environment Variable:

(Optional) Note: This could potentially be replaced with Google Cloud Secret Manager.
```powershell
$env:GOOGLE_APPLICATION_CREDENTIALS
```
### Verify Environment Variable:

(Optional) Note: This could potentially be replaced with Google Cloud Secret Manager.
```powershell
$env:GOOGLE_APPLICATION_CREDENTIALS
```
## Step 3: Set Up Maven Project
### Add the dependencies in the Maven Project:
Open the SOF project with dependencies.
```
<project xmlns="http://maven.apache.org/POM/4.0.0"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>demo</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>
    <name>demo</name>
    <url>http://maven.apache.org</url>
    <dependencies>
        <!-- Google Cloud Storage dependency -->
        <dependency>
            <groupId>com.google.cloud</groupId>
            <artifactId>google-cloud-storage</artifactId>
            <version>2.1.5</version>
        </dependency>
        <dependency>
            <groupId>commons-io</groupId>
            <artifactId>commons-io</artifactId>
            <version>2.8.0</version>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>3.8.1</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```
### Step 4: Use the Below Java Code to Upload File to GCP Storage Bucket:
## Create the below Java Class
```
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;

public class SimpleInterface {

    public SimpleInterface() {
        JFrame frame = new JFrame("File Upload Example");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);

        JLabel label = new JLabel("This is a test for moving reference from SMB Mount to Cloud Storage");
        panel.add(label);

        JButton button = new JButton("Upload File");
        panel.add(button);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    java.io.File file = fileChooser.getSelectedFile();
                    try {
                        uploadToGCS(file.getAbsolutePath(), "your-bucket-name", file.getName());
                        JOptionPane.showMessageDialog(frame, "File uploaded!");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "File upload failed: " + ex.getMessage());
                    }
                }
            }
        });

        frame.setVisible(true);
    }

    private void uploadToGCS(String filePath, String bucketName, String objectName) throws IOException {
        Storage storage = StorageOptions.getDefaultInstance().getService();
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, objectName).build();
        storage.create(blobInfo, new FileInputStream(filePath));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SimpleInterface();
            }
        });
    }
}
```
### Run the New build
