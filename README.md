![Latest Release](https://img.shields.io/github/v/release/FooqX/GoldenRatio?style=for-the-badge&logo=github)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-orange.svg?style=for-the-badge)](https://choosealicense.com/licenses/gpl-3.0/)

# Golden Ratio

Golden Ratio is a FOSS Android calculator application, that helps you to calculate a, b, sum using the [Golden Ratio](https://en.wikipedia.org/wiki/Golden_ratio).

## Installation

Install [Droidify](https://github.com/Droid-ify/client), or add IzzyOnDroid's repo to F-Droid, then search for this app and install it there. (PENDING)

Alternatively, you could directly download the APK file from releases and update that way too.
## Usage
This is a special calculator, not just a basic a+b calculator.
It calculates based on the input, and fills the blank spaces. It **can** calculate:

1. **A and B** based on the sum value;
2. **A and Sum** based on B value;
3. **B and Sum** based on A value;

So, that means you're only allowed to enter:

- A
- B
- SUM,

and the rest it will automatically calculate using the Golden ratio and basic math (subtraction, addition).

To clear the entered values after they have been calculated, click the "Calculate" button again. The "Clear" button only clears the history.
## Features
- No ads, tracking.
- Free and open source (FOSS)
- Fully translated in English, Latvian.
- Shows calculation history
- Calculate a, b, sum easily using Golden Ratio
- Shows which quantities were calculated using the Golden ratio in history (it's denoted by the φ symbol)
- Simple interface

## Screenshots
English:


![image](https://github.com/user-attachments/assets/c7e38c84-6114-4037-a94c-46eaa84b15b8)


Latvian:


![image](https://github.com/user-attachments/assets/02524e7d-4c80-41aa-b8c8-94073c672204) 

![screen-20250323-131056~2 (2)](https://github.com/user-attachments/assets/1a3c39bb-f799-4296-9e76-d353d35afc28)


## How it works
INV_PHI = 1/PHI = 0.618...


PHI = 1.618...

If you enter only Sum, then:
```kotlin
val a = total * INV_PHI
val b = total - a
```

If you enter only A:
```kotlin
val b = a / PHI
val total = a + b
```

If you enter only B:
```kotlin
val a = b * PHI
val total = a + b
```
## Contributing

Pull requests are welcomed! For major changes, please open an issue to discuss what you would like to change/add/improve.

## License

[GNU General Public License v3.0](https://choosealicense.com/licenses/gpl-3.0/)
