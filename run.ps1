# Compila y ejecuta App.java (menu interactivo) usando el JDK 21 del proyecto.
Set-Location $PSScriptRoot
$JDK = "C:\Users\cinti\.vscode\extensions\redhat.java-1.55.0-win32-x64\jre\21.0.11-win32-x86_64\bin"

& "$JDK\javac.exe" -d bin -cp "lib/gson-2.10.1.jar" -sourcepath src src/App.java
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
& "$JDK\java.exe" -cp "bin;lib/gson-2.10.1.jar" App
