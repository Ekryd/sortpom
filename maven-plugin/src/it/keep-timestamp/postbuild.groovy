log = new File(basedir, 'build.log')
sorted = new File(basedir, 'pom.xml')
backup = new File(basedir, 'pom.xml.bak')
timestampFile = new File(basedir, 'pom-timestamp')

assert log.exists()
assert log.text.contains('Sorting file ' + sorted.absolutePath)
assert log.text.contains('Saved backup of ' + sorted.absolutePath + ' to ' + backup.absolutePath)
assert log.text.contains('Saved sorted pom file to ' + sorted.absolutePath)
assert log.text.contains('keepTimestamp = true')

timestamp = timestampFile.getText('UTF-8') as Long
assert sorted.lastModified() == timestamp
// Do not assert anything about the backup file, since that timestamp is OS dependent

return true
