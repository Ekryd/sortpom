log = new File(basedir, 'build.log');
sorted = new File(basedir, 'src/main/resources/pom.xml');
expected = new File(basedir, 'src/test/resources/pom.xml');
backup = new File(basedir, 'src/main/resources/pom.xml.bak');

assert log.exists();
assert log.text.contains('Sorting file ' + sorted.absolutePath);
assert log.text.contains('Saved backup of ' + sorted.absolutePath + ' to ' + backup.absolutePath);
assert log.text.contains('Saved sorted pom file to ' + sorted.absolutePath);
assert backup.exists();
assert expected.text.tokenize('\n').equals(sorted.text.tokenize('\n'));

return true;
