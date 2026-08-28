/**
 * Post-processes generated MDX files to fix broken HTML entities.
 * The OpenAPI plugin sometimes generates unescaped angle brackets from
 * spec descriptions (e.g., "<use namespace from context>") which break
 * MDX parsing. This script escapes them.
 */
const fs = require('fs');
const path = require('path');

function walkDir(dir, callback) {
  if (!fs.existsSync(dir)) return;
  fs.readdirSync(dir).forEach(f => {
    const filePath = path.join(dir, f);
    if (fs.statSync(filePath).isDirectory()) {
      walkDir(filePath, callback);
    } else if (f.endsWith('.mdx')) {
      callback(filePath);
    }
  });
}

let fixedCount = 0;
walkDir(path.join(__dirname, '..', 'docs', 'api'), (filePath) => {
  const content = fs.readFileSync(filePath, 'utf8');
  // Fix broken angle brackets: <something&gt; → &lt;something&gt;
  const fixed = content.replace(/<([^/>\n][^>\n]*?)&gt;/g, '&lt;$1&gt;');
  if (fixed !== content) {
    fs.writeFileSync(filePath, fixed);
    fixedCount++;
    console.log('Fixed:', path.relative(process.cwd(), filePath));
  }
});

if (fixedCount > 0) {
  console.log(`Fixed ${fixedCount} file(s)`);
} else {
  console.log('No MDX fixes needed');
}
