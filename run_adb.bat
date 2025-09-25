
REM download platform tools from SDK MANAGER
REM come consigliato qui
REM https://developer.android.com/studio/releases/platform-tools
REM Viene installato sotto
REM C:\Users\Developer\AppData\Local\Android\Sdk\platform-tools

REM AGGIUNTA VAR AMBIENTE
REM cd C:\Users\Developer\AppData\Local\Android\Sdk\platform-tools
REM dir
REM ./adb.exe logcat -v threadtime >D:/logcat.txt
REM dir adb.exe
ECHO %PATH%
pause

adb.exe logcat -v threadtime >D:/logcat.txt
pause