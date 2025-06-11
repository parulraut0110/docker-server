from flask import Flask, request, jsonify
import ssl

app = Flask(__name__)

@app.route('/welcome', methods=['POST'])
def welcome():
    data = request.get_json()
    full_name = data.get('fullName', '')
    first_name = full_name.split()[0] if full_name else 'guest'
    return jsonify ({"message": f"Hello {first_name}"})

if __name__ == "__main__":
    context = ssl.SSLContext(ssl.PROTOCOL_TLS_SERVER)
    context.load_cert_chain(certfile="python-server.crt", keyfile="python-server.key")
    app.run(host="0.0.0.0", port=5000, ssl_context=context, debug=True)