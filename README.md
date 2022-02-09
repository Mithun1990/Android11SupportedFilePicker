# Android11SupportedFilePicker
## The Library has developed to get file using picker intent for supporting android 11 OS and other version (higher + lower).
> Step 1. Add the JitPack repository to your project build.gradle and settings.gradle file 
```gradle 
allprojects {
		repositories {	
			maven { url 'https://jitpack.io' }
		}
	}
  ```
>Step 2. Add the dependency in app level build.gradle 
```gradle
dependencies {
	   implementation 'com.github.Mithun1990:Android11SupportedFilePicker:1.0.0-SNAPSHOT-1'
	}
  ```
>Step 3. Register an observer with higher order callback function to get the file during oncreate method of activity or fragment
```kotlin
        val observer =
            FilePickerLifeCycleObserver(
                this@MainActivity,
                this.activityResultRegistry) 
            { supportedFile, supportedFileList ->
                /* Single File Selection result*/
                supportedFile?.let {
                    println("Relative file path " + it.file + " " + it.fileName)
                }
                /* Multiple File Selection result*/
                supportedFileList?.let {
                    for (i in it) {
                        println("Relative file path " + i.file + " " + i.fileName)
                    }
                }
            }
        lifecycle.addObserver(observer)
```
>Step 4. Call the file picker intent by click or upon nceessary with file type and multiple file selection enable
>>Long press on the file selecting multiple files
```
observer.getFilePickerIntent(
                mutableListOf(
                    SupportedFileAnnotationType.Type.IMAGE,
                    SupportedFileAnnotationType.Type.PDF,
                ),
                true 
            )
 ```
