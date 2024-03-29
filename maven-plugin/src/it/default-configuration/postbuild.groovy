log = new File(basedir, 'build.log')
sorted = new File(basedir, 'pom.xml')
expected = new File(basedir, 'expected_pom.xml')
backup = new File(basedir, 'pom.xml.bak')

assert log.exists()
assert log.text.contains('Sorting file ' + sorted.absolutePath)
assert log.text.contains('Saved backup of ' + sorted.absolutePath + ' to ' + backup.absolutePath)
assert log.text.contains('Saved sorted pom file to ' + sorted.absolutePath)

assert log.text.contains('pomFile = ' + sorted.absolutePath)
assert log.text.contains('createBackupFile = true')
assert log.text.contains('backupFileExtension = .bak')
assert log.text.contains('encoding = UTF-8')
assert log.text.contains('lineSeparator = ')
assert log.text.contains('expandEmptyElements = true')
assert log.text.contains('spaceBeforeCloseEmptyElement = false')
assert log.text.contains('keepBlankLines = true')
assert log.text.contains('endWithNewline = true')
assert log.text.contains('nrOfIndentSpace = 2')
assert log.text.contains('ignoreLineSeparators = true')
assert log.text.contains('indentBlankLines = false')
assert log.text.contains('indentSchemaLocation = false')
assert log.text.contains('predefinedSortOrder default-value="recommended_2008_06"')
assert !log.text.contains('sortOrderFile =')
assert !log.text.contains('sortDependencies =')
assert !log.text.contains('sortPlugins =')
assert log.text.contains('sortProperties = false')
assert log.text.contains('sortModules = false')
assert log.text.contains('sortExecutions = false')
assert log.text.contains('skip = false')
assert log.text.contains('keepTimestamp = false')


assert backup.exists()
assert expected.text.replaceAll('@pom.version@', projectversion).tokenize('\n').equals(sorted.text.replaceAll('\r','').tokenize('\n'))

return true
