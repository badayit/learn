# Patente Facile

App per preparare l'esame di teoria della patente B in Italia, con traduzione e spiegazioni in arabo.
تطبيق للتحضير لامتحان النظري لرخصة السياقة B في إيطاليا، مع الترجمة والشرح بالعربية.

- **Simulazione d'esame**: 30 domande Vero/Falso, 20 minuti, massimo 3 errori, grafico dei risultati.
- **Corsi**: lezioni per argomento con audio (italiano + arabo).
- **Quiz per argomento**, **Segnali**, **Errori** da ripassare.
- **Profilo**: nome, Gmail o telefono, password, foto, data dell'esame. Più profili sullo stesso telefono.
- **Impostazioni**, **Privacy**, **Termini**, **Info**.
- Tutto resta sul dispositivo: nessun server, nessuna pubblicità.

## File

| Percorso | Cosa contiene |
|---|---|
| `app.html` | **Sorgente unica** dell'app (HTML/CSS/JS) |
| `build.sh` | Genera `index.html`, `privacy.html` e la copia per Android |
| `index.html`, `manifest.json` | Versione web |
| `privacy.html` | Informativa privacy e termini (URL per Google Play) |
| `android/` | Progetto Android Studio (WebView offline, audio con il TTS di Android) |
| `store/` | Icona, feature graphic, screenshot e guida alla pubblicazione (`store/PUBLISHING.md`) |

Dopo ogni modifica ad `app.html`: `./build.sh`, poi in `android/`: `./gradlew bundleRelease`.
