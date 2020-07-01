log = new File(basedir, 'build.log')
backup = new File(basedir, 'pom.xml.bak')

assert log.exists()
assert log.text.contains('Skipping Sortpom')
assert !backup.exists()

return true
