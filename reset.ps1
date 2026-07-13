# Borra bin/, recompila todo y vuelve a correr los tests JUnit con el JDK 21.
Set-Location $PSScriptRoot
$JDK = "C:\Users\cinti\.vscode\extensions\redhat.java-1.55.0-win32-x64\jre\21.0.11-win32-x86_64\bin"

if (Test-Path bin) { Remove-Item bin -Recurse -Force }
$srcs = Get-ChildItem -Recurse src -Filter *.java | ForEach-Object { $_.FullName }
& "$JDK\javac.exe" -d bin -cp "lib/*" -sourcepath src $srcs
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
& "$JDK\java.exe" -jar "lib/junit-platform-console-standalone-1.10.0.jar" -cp "bin;lib/gson-2.10.1.jar" --scan-classpath bin
