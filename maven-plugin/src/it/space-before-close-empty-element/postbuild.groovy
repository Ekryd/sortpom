log = new File(basedir, 'build.log')
sorted = new File(basedir, 'pom.xml')
expected = new File(basedir, 'expected_pom.xml')
backup = new File(basedir, 'pom.xml.bak')

assert log.exists()
assert log.text.contains('Sorting file ' + sorted.absolutePath)
assert log.text.contains('Saved backup of ' + sorted.absolutePath + ' to ' + backup.absolutePath)
assert log.text.contains('Saved sorted pom file to ' + sorted.absolutePath)
assert backup.exists()

expectedText = expected.text.replaceAll('@pom.version@', projectversion)
sortedText = sorted.text.replaceAll('\r','')
assert sortedText == expectedText

return true
