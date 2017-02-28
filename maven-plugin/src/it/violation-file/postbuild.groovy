log = new File(basedir, 'build.log')
sorted = new File(basedir, 'pom.xml')
violationFile = new File(basedir, 'sortpom-reports/sortpom-result.xml')
expected_violationFile = new File(basedir, 'expected_sortpom-result.xml')

assert log.exists()
assert log.text.contains('Sorting file ' + sorted.absolutePath)
assert log.text.contains('Saved backup of ' + sorted.absolutePath + ' to ' + backup.absolutePath)
assert log.text.contains('Saved sorted pom file to ' + sorted.absolutePath)

assert expected.text.replaceAll('@pom.version@', projectversion).tokenize('\n').equals(sorted.text.tokenize('\n'))

return true