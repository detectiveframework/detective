# Scenario Table

A scenario table will cause a scenario run **multipule** times **concurrently** based on rows on that table.

senario table parses on DSL level, which means the values are settled before a scenario runs. for each rows, it may run in different machine potential (for example a spark cluster)
