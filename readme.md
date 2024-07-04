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
$env:GOOGLE_APPLICATION_CREDENTIALS="C:\path\to\your\service-account-file.json"```

### Verify Environment Variable:**
(Optional) Note: This could potentially be replaced with Google Cloud Secret Manager.
```powershell
$env:GOOGLE_APPLICATION_CREDENTIALS```

