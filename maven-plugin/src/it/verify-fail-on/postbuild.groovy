log = new File(basedir, 'build.log')
sorted = new File(basedir, 'pom.xml')
violationFile = new File(basedir, 'target/sortpom_reports/violation.xml')
expected_violationFile = new File(basedir, 'expected_violation_file.xml')

assert log.exists()
assert log.text.contains('[INFO] Verifying file ' + sorted.absolutePath)
assert log.text.contains('[WARNING] The xml element <modelVersion> should be placed before <name>')
assert log.text.contains('[INFO] Saving violation report to ' + violationFile.absolutePath)
assert log.text.contains('[WARNING] The file ' + sorted.absolutePath + ' is not sorted')

assert expected_violationFile.text.replaceAll('@POM_PATH@', sorted.absolutePath).tokenize('\n').equals(violationFile.text.replaceAll('\r','').replaceAll('\\\\','').tokenize('\n'))

return true
