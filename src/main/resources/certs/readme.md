
# Generate public/private keys via OpenSSL

__GENERATE A KEY PAIR__:

```openssl genrsa -out keypair.pem 2048```

__GENERATE THE PUBLIC KEY__:

```openssl rsa -in keypair.pem -pubout -out public.pem```

__GENERATE THE PRIVATE KEY2__:

```openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem```

SYMMETRIC KEY:
NimbusJwtDecoder.withJwkSetUri
