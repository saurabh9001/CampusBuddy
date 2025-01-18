# CampusBuddy

## Project Overview
A comprehensive Android application designed to enhance campus life and student productivity.

## Project Structure
mysql5/
│
├── app/                    # Main Android application module
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/       # Kotlin source code
│   │   │   ├── res/        # Resource files
│   │   │   └── AndroidManifest.xml
│   │   └── test/           # Unit and instrumentation tests
│
├── scripts/                # Utility scripts
│   └── convert_to_webp.sh
│
├── config/                 # Configuration files
│   ├── google-services.json
│   └── local.properties
│
├── docs/                   # Documentation
│
├── gradle/                 # Gradle wrapper
│
└── README.md               # Project documentation

## Key Features
- 🔐 User Authentication (Firebase)
- 📅 Class Scheduling
- 📝 Notes Management
- 📄 PDF Viewer
- 🧮 Calculator
- 🔔 Reminders

## Technologies
- Language: Kotlin
- Authentication: Firebase
- Build System: Gradle
- Platform: Android

## Setup Instructions
1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Add your `google-services.json` to the `config/` directory
5. Build and run the project

## Contributing
1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License
Distributed under the MIT License. See `LICENSE` for more information.
