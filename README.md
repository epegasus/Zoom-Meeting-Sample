# Zoom Meeting SDK - Android Application

This project is an Android application that integrates Zoom Meeting SDK, providing users with the ability to **Start a Meeting** or **Join a Meeting** directly from the app. The implementation of Zoom SDK covers both authentication methods and meeting operations.

## Features
- **Start Meeting**: Authenticate using JWT and OAuth to start a Zoom meeting.
- **Join Meeting**: Quickly join a Zoom meeting by entering the meeting ID and password without requiring any additional authentication.

![ss_1](https://github.com/user-attachments/assets/8bba0273-c97f-433c-9643-66cb15cdcfd6)
![ss_2](https://github.com/user-attachments/assets/fe3e277b-60c3-4895-96f7-b71ded618425)


## Getting Started
To get started, clone the repository and follow the setup instructions provided below.

### Start Meeting Flow

To start a meeting using the Zoom SDK, the following steps are involved:

#### Step 1: Create a Zoom Marketplace App
- Login to [Zoom Marketplace](https://marketplace.zoom.us/).
- Click on **Build App** under the "Develop" button (top-right corner).

![App type](https://github.com/user-attachments/assets/3c8c7f3f-9e4d-4a85-9cfb-fa0c130b7425)

- Select **General App** from the available options and fill in the required credentials.
  - You'll obtain `client_id` and `client_secret`, which are required for authentication.
  - Define the following scopes:
    - `user:read:user`
    - `user:read:token`
    - `user:read:zak`
    - `zoomapp:inmeeting`

![Scopes](https://github.com/user-attachments/assets/37d30b40-e7ec-4efa-84d4-55eca978ae11)

#### Step 2: Get JWT Token
- After the console setup, generate a JWT token using the `client_id` and `client_secret`.
- You can use the library dependency [https://github.com/auth0/java-jwt](https://github.com/auth0/java-jwt) to do this.

#### Step 3: Get Authorization Code
- Request the authorization code by making a GET request to the following URL:
  ```
  https://zoom.us/oauth/authorize?response_type=code&client_id=${CLIENT_ID}&redirect_uri=$redirectUri
  ```

#### Step 4: Get Access Token
- After allowing the application to access Zoom, use the authorization code to obtain an access token:
  ```
  curl -X POST https://zoom.us/oauth/token \
    -H "Authorization: Basic $(echo -n 'YOUR_CLIENT_ID:YOUR_CLIENT_SECRET' | base64)" \
    -d "grant_type=authorization_code" \
    -d "code=YOUR_AUTHORIZATION_CODE" \
    -d "redirect_uri=YOUR_REDIRECT_URI"
  ```
- Expected response:
  ```json
  {
    "access_token": "ACCESS_TOKEN",
    "token_type": "bearer",
    "expires_in": 3600,
    "refresh_token": "REFRESH_TOKEN",
    "scope": "user:read"
  }
  ```

#### Step 5: Get ZAK Token
- Use the access token to obtain a ZAK token for starting a meeting:
  ```
  curl -X GET "https://api.zoom.us/v2/users/me/token?type=zak" \
    -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
  ```
- Expected response (success):
  ```json
  {
    "token": "ZAK_TOKEN"
  }
  ```

#### Step 6: Start Meeting
- With the ZAK token, you can now start the meeting using the Zoom SDK.

### Join Meeting Flow
To join a meeting, simply enter the **Meeting ID** and **Password** and click the **Join** button. No authentication is required for this process.

## Note
- To download the Zoom Meeting SDK, you need to create an app in Zoom Marketplace as a "General App".
- Navigate to **Build App** > **Features** > **Embed** > **Meeting SDK** > **Android**.
- Download the latest version of the project and extract the `mobilertc` module. Import the module into your Android project, and you're good to go!

## Dependencies
- [Java JWT (Auth0)](https://github.com/auth0/java-jwt) for generating JWT tokens.
- Zoom Meeting SDK for Android (requires creating an app on Zoom Marketplace).

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact
For any questions or issues, please feel free to open an issue or reach out via GitHub.

Happy Coding!

