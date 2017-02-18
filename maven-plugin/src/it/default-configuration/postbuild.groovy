log = new File(basedir, 'build.log');
sorted = new File(basedir, 'pom.xml');
expected = new File(basedir, 'src/test/resources/pom.xml');
backup = new File(basedir, 'pom.xml.bak');

assert log.exists();
assert log.text.contains('Sorting file ' + sorted.absolutePath);
assert log.text.contains('Pom file is already sorted, exiting');
assert !backup.exists();
assert expected.text.replaceAll('@pom.version@', projectversion).tokenize('\n').equals(sorted.text.tokenize('\n'));

return true;