
set DIR_TARGET=C:\MyWork\Progetti\SourceAndroidStudio_2024\Meditazione-iricostruttori-resp\app\build\outputs\apk
set FILE_INST=app-iricostruttori.jks

REM copy D:\MyWork\Progetti\SourceAndroidStudio_2024\Meditazione-iricostruttori-resp\app\build\outputs\apk\app-iricostruttori.jks
copy %FILE_INST% %DIR_TARGET%\%FILE_INST% 
cd %DIR_TARGET%
dir %FILE_INST%
pause