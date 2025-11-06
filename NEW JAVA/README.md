# Wedding Hall Booking System (Java AWT)

## Features
- Manage halls: name, capacity, price/hour
- Create bookings with overlap checks per hall/date/time
- Calculate hours and pricing
- View, edit, delete bookings
- CSV file persistence under `data/`

## Requirements
- Java 8 or newer

## Compile
```
javac -d out -sourcepath src src/com/wedding/Main.java
```

## Run
```
java -cp out com.wedding.Main
```

Data files will be created in `data/halls.csv` and `data/bookings.csv` in the working directory.
