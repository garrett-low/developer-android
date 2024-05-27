https://www.cityofmadison.com/metro/business/information-for-developers

05/26/2024 21:41:45→ sqlite3.exe
SQLite version 3.42.0 2023-05-16 12:36:15
Enter ".help" for usage hints.
Connected to a transient in-memory database.
Use ".open FILENAME" to reopen on a persistent database.
sqlite> .open mmt_gtfs_from_csv_2024_03_22
sqlite> .mode csv
sqlite> .import --csv "C:\git\developer-android\mmt_gtfs\trips.csv" trips
Error: cannot open "C:gitdeveloper-androidmmt_gtfs      rips.csv"
sqlite> .import --csv "C:/git/developer-android/mmt_gtfs/trips.csv" trips
sqlite> .import --csv "C:/git/developer-android/mmt_gtfs/stops.csv" stops
sqlite> .import --csv "C:/git/developer-android/mmt_gtfs/routes.csv" routes
sqlite> .import --csv "C:/git/developer-android/mmt_gtfs/shapes.csv" shapes
sqlite> .quit