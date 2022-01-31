# AndroidFilePicker
> Step 1. Add the JitPack repository to your project build.gradle file 
```gradle 
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  ```
>Step 2. Add the dependency in app level build.gradle 
```gradle
dependencies {
	        implementation 'com.github.Mithun1990:Android11SupportedFilePicker:1.0.0-alpha01'
	}
  ```
>Step 3. Register an observer with higher order callback function to get the file during oncreate method of activity or fragment
```kotlin
val observer =
            FileManagerLifeCycleObserver(
                this@MainActivity,
                this.activityResultRegistry,
                { supportedFile -> println("Relative file path " + supportedFile.file + " " + supportedFile.fileName) }
            )
lifecycle.addObserver(observer)
```
>Step 4. Call the file picker intent by click or upon nceessary
```
observer.getFilePickerIntent(
                mutableListOf(
                    SupportedFileAnnotationType.Type.IMAGE,
                    SupportedFileAnnotationType.Type.PDF,
                )
            )
 ```
