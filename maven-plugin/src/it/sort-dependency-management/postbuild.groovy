log = new File(basedir, 'build.log')
sorted = new File(basedir, 'pom.xml')
expected = new File(basedir, 'expected/pom.xml')
backup = new File(basedir, 'pom.xml.bak')

assert log.exists()
assert log.text.contains('Sorting file ' + sorted.absolutePath)
assert log.text.contains('Saved backup of ' + sorted.absolutePath + ' to ' + backup.absolutePath)
assert log.text.contains('Saved sorted pom file to ' + sorted.absolutePath)
assert backup.exists()
assert expected.text.replaceAll('@pom.version@', projectversion).tokenize('\n').equals(sorted.text.replaceAll('\r','').tokenize('\n'))

return true