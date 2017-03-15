server {
    port = 8080 //change this if you need.
}

googleauth {
    clientId = '896100416043-v0cvdf52tteag7ha8939fog24sr7bm2g.apps.googleusercontent.com'
    issuer = 'accounts.google.com'
}

dataSource {
    pooled = true
    jmxExport = true
    driverClassName = 'com.mysql.jdbc.Driver'
//    dialect = 'org.hibernate.dialect.MySQL5InnoDBDialect'
    username = 'root'
    password = 'smb3pwns'
}

environments {

    development {
        dataSource {
            dbCreate = 'create-drop'
            url = 'jdbc:mysql://localhost:3306/user_database'
        }
    }

    test {
        dataSource {
            dbCreate = 'update'
            url = 'jdbc:mysql://localhost:3306/user_database'
        }
    }

    production {
        dataSource {
            dbCreate = 'none'
            url = 'jdbc:mysql://localhost:3306/user_database'
            properties {
                jmxEnabled = true
                initialSize = 5
                maxActive = 50
                minIdle = 5
                maxIdle = 25
                maxWait = 10000
                maxAge = 600000
                timeBetweenEvictionRunsMillis = 5000
                minEvictableIdleTimeMillis = 60000
                validationQuery = 'SELECT 1'
                validationQueryTimeout = 3
                validationInterval = 15000
                testOnBorrow = true
                testWhileIdle = true
                testOnReturn = false
                jdbcInterceptors = 'ConnectionState'
                defaultTransactionIsolation = '2 # TRANSACTION_READ_COMMITTED'
            }
        }
    }

}
