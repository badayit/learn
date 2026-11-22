// Renders the privacy policy and terms from app.html (APP + DOCS) into a standalone page.
const fs = require("fs");
const src = fs.readFileSync(__dirname + "/../app.html", "utf8");
const start = src.indexOf("const APP = "), end = src.indexOf("/* ---------- Storage");
const { APP, DOCS } = new Function(src.slice(start, end) + "; return { APP, DOCS };")();
const esc = s => String(s).replace(/[&<>"]/g, c => ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;" }[c]));
const part = (d, lang) => `<section class="${lang}" lang="${lang}" dir="${lang === "ar" ? "rtl" : "ltr"}"><h2>${esc(lang === "ar" ? d.ta : d.t)}</h2>` +
  d[lang].map(([h, b]) => `<h3>${esc(h)}</h3><p>${esc(b())}</p>`).join("") + `</section>`;
process.stdout.write(`<!doctype html>
<html lang="it"><head><meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>${APP.name} · Privacy</title>
<style>
:root{--bg:#EDF1F7;--fg:#0F1B2D;--muted:#56647C;--blue:#0B4EA2;--card:#fff;--line:#D3DBE7}
@media (prefers-color-scheme:dark){:root{--bg:#0A111D;--fg:#E5EBF4;--muted:#8E9BB2;--blue:#6FA2F0;--card:#131C2C;--line:#243149;color-scheme:dark}}
body{margin:0;background:var(--bg);color:var(--fg);font:16px/1.6 system-ui,-apple-system,"Segoe UI",Roboto,"Noto Sans Arabic",sans-serif}
main{max-width:720px;margin:0 auto;padding:24px max(16px,env(safe-area-inset-left,0px)) 48px}
header{display:flex;gap:12px;align-items:center;margin-bottom:8px}header b{font-size:22px}
nav{display:flex;gap:16px;flex-wrap:wrap;margin:8px 0 20px}nav a{color:var(--blue);font-weight:600}
section{background:var(--card);border:1px solid var(--line);border-radius:14px;padding:4px 20px 16px;margin-bottom:16px}
h2{color:var(--blue);margin:16px 0 4px}h3{margin:16px 0 2px;font-size:17px}p{margin:0}.ar{text-align:right}small{color:var(--muted)}
</style></head><body><main>
<header><b>${APP.name}</b><small>v${APP.version}</small></header>
<nav><a href="#privacy">Privacy</a><a href="#termini">Termini d'uso</a><a href="#ar">العربية</a></nav>
<div id="privacy">${part(DOCS.privacy, "it")}</div><div id="termini">${part(DOCS.termini, "it")}</div>
<div id="ar">${part(DOCS.privacy, "ar")}${part(DOCS.termini, "ar")}</div>
<small>Ultimo aggiornamento: ${APP.updated} · ${esc(APP.email)}</small>
</main></body></html>
`);
