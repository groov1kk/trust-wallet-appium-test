## Trust Wallet Appium tests

To run tests use the following command:

```
$ ./mvnw clean test
```

Notice, that .apk file with the application is not stored in this repo. To set the path to the application you need to
set the property ```app.path```. You can do it either specifying the value of this property in the *test.properties*
file or passing the path as a parameter to run command:

```
$ ./mwnw cleat test -Dapp.path=/path/to/your/application
```

The path may be relative or absolute.

The same with a ```device.name```, which is responsible to a device (or emulator) where you would like to run these
tests: either set the value in the *test.properties* file or passing the path as a parameter to run command:

```
$ ./mwnw clean test -Ddevice.name=YourAwsomeDevice
```

## Test scenarios

List of test cases can be found [here](./doc/test_scenarios.md).