# Patente Facile: kifach tnchrha f Google Play

## 0. 9bel kolchi (darouri)

1. F `patente/app.html`, f ster `const APP = {...}`, bdel:
   - `developer`: smiytek wla smiya dyal l'entreprise (kifma ghadi tbane f Google Play).
   - `email`: email dyal contact. Google Play kaytleb email 3am.
2. Dir `./build.sh` mn dossier `patente/`. Kaysawb mn jdid `index.html`, `privacy.html` w l'copie dyal Android.
3. Bdel `applicationId` f `android/app/build.gradle` ila bghiti (daba `com.baday.patentefacile`). **Mn b3d l'publication ma yb9ach ytbeddel.**

## 1. Lien dyal Privacy Policy (Google Play kaytlbou)

Google Play bgha URL public. Ashel tari9a: GitHub Pages.
1. Dmej branch f `main`.
2. GitHub > repo `learn` > Settings > Pages > Source: `main`, dossier `/ (root)`.
3. L'lien ghadi ykoun: `https://badayit.github.io/learn/patente/privacy.html`
   (l'app 7ta hiya ghadi tkhdem f `https://badayit.github.io/learn/patente/`).

Ila kan repo private, GitHub Pages kaykhess compte payant. Wla 7ett `privacy.html` f ay site public akhor.

## 2. Bni l'app (AAB) b Android Studio

1. 7ell dossier `patente/android` f Android Studio.
2. **Sawb clé de signature (upload key) merra wa7da:** Build > Generate Signed App Bundle > Create new…
   Khbbi fichier `.jks` w les mots de passe f blasa mzyana. **Ila ddi3ti, ma t9derch tpublia mises à jour.**
3. Sawb fichier `android/keystore.properties` (ma kaytcommitach):
   ```
   storeFile=/chemin/vers/upload.jks
   storePassword=...
   keyAlias=upload
   keyPassword=...
   ```
4. Build: `./gradlew bundleRelease` → `app/build/outputs/bundle/release/app-release.aab`
5. Kol mise à jour: zid `versionCode` (2, 3, …) w bdel `versionName` f `app/build.gradle`.

Jerreb 9bel 3la telephone dyalk: `./gradlew installDebug` (USB debugging m7lol).
Chouf b lkhsous: l'audio (ttalyaniya w l3arbiya), tswira dyal profil, bouton retour, w dark mode.

## 3. Google Play Console

- Compte développeur: 25$ merra wa7da. Comptes personnels jdad khasshom **test fermé b 12 testeurs l mouddat 14 yom** 9bel production.
- **App category:** Education. **Gratuit wla payant:** chouf section 6.
- **Fichiers f `store/`:**
  - `icon-512.png`: icône
  - `feature-graphic-1024x500.png`: feature graphic
  - `screen-1…6.png`: captures telephone (1080×1920)

### Data safety (sécurité des données)
- L'app katjm3 wla katpartagi données? **La** (kolchi kaybqa f telephone, ma kayt'sifet walou).
- Ila bdelti chi 7aja (pub, analytics, login Firebase…), khassek tbeddel had jawab w privacy policy.

### Content rating (IARC)
Category: Education/Reference. Ma fiha la 3onf, la jins, la chat, la achat, la pub. Ghadi tkhrej "Everyone / PEGI 3".

### Target audience
Khtar **18+** (wla 16+). Ma tkhtarch a9al mn 13, bach ma tdkholch f règles "Families".

### Government apps
Lazem f description tgoul anna l'app **machi officielle**. (Ra hadchi kayn f l'app f "Info" w f "Termini".)

## 4. Texte dyal listing

**Nom (30 max):** `Patente Facile – Quiz B`

**Description courte (80 max)**
- IT: `Quiz patente B Vero/Falso con corsi audio e traduzione in arabo. Offline.`
- AR: `اختبارات رخصة السياقة الإيطالية B مع دروس صوتية وترجمة عربية، بدون إنترنت.`

**Description complète (IT)**
```
Preparati all'esame di teoria della patente B con domande Vero/Falso, lezioni con audio e traduzione in arabo.

• Simulazione d'esame: 30 domande, 20 minuti, massimo 3 errori, come all'esame vero.
• Corsi: 11 argomenti spiegati in modo semplice (segnali, precedenza, velocità, sorpasso, sosta, luci, autostrada, punti e alcol, sicurezza, primo soccorso, veicolo).
• Audio: ascolta lezioni e domande in italiano e in arabo.
• Quiz per argomento con spiegazione dopo ogni risposta.
• Ripasso errori: le domande sbagliate restano da ripassare finché non le impari.
• Segnali stradali principali con significato in italiano e arabo.
• Grafico dei progressi e conto alla rovescia per il giorno dell'esame.
• Profili per più persone sullo stesso telefono, protetti da password.
• Funziona senza internet. Nessuna pubblicità, nessun dato inviato.

App non ufficiale: non è collegata al Ministero delle Infrastrutture e dei Trasporti né alla Motorizzazione Civile. Le domande sono scritte in modo indipendente sulla base del Codice della Strada e non sono le domande ufficiali dell'esame.
```

**Description complète (AR)**
```
استعد لامتحان النظري لرخصة السياقة B في إيطاليا بأسئلة صحيح/خطأ، دروس صوتية وترجمة عربية.

• امتحان تجريبي: 30 سؤالاً، 20 دقيقة، 3 أخطاء على الأكثر، بحال الامتحان الحقيقي.
• دروس: 11 موضوعاً مشروحاً ببساطة (العلامات، الأولوية، السرعة، التجاوز، الوقوف، الأضواء، الطريق السيار، النقاط والكحول، السلامة، الإسعافات، السيارة).
• الصوت: اسمع الدروس والأسئلة بالإيطالية والعربية.
• اختبارات حسب الموضوع مع الشرح بعد كل جواب.
• مراجعة الأخطاء حتى تتعلمها.
• أهم علامات الطريق مع معناها بالإيطالية والعربية.
• رسم بياني للتقدم وعدّ تنازلي ليوم الامتحان.
• حسابات لعدة أشخاص في نفس الهاتف، محمية بكلمة سر.
• يعمل بدون إنترنت. بدون إعلانات، وبدون إرسال أي معلومات.

تطبيق غير رسمي: لا علاقة له بوزارة النقل الإيطالية ولا بـ Motorizzazione Civile. الأسئلة مكتوبة بشكل مستقل اعتماداً على قانون السير وليست الأسئلة الرسمية للامتحان.
```

## 5. Ila bdelti l'app mn b3d

- `app.html` howa source wa7ed. Mn b3d kol modification: `./build.sh` w 3awd build dyal Android.
- `store/` images: `node` script f scratchpad tsawbat bih. Ila bdelti design, 3awd captures.

## 6. Bach trbe7 (n9at mohimma)

- **L'contenu:** daba fiha 128 so2al, 54 qa3ida w 14 3alama. Apps l'concurrents fihom alaf dyal l'as2ila w ktirin mjanin. Bach ynass ykhelsou, khass tzid bzaf dyal l'as2ila w l'3alamat (300–1000+), w traj3ha m3a moniteur dyal auto-école.
- **Model:** ashel l'bda: app mjania + "Premium" (achat intégré) kay7ell examens bla 7dod w ga3 l'as2ila. Wla app payante (1–3€). Ila zedti pub wla achats, khassek tbeddel **Data safety** w **privacy policy**.
- **Smiya:** chouf f Google Play wach kayna chi app b smiya "Patente Facile". Ila kayna, khtar smiya khra bach tbe3ed mn mochkil dyal marque.
