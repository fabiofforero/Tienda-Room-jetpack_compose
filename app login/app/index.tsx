import { router } from "expo-router";
import { useState } from "react";
import { Alert, Button, Text, TextInput, View } from "react-native";

const LoginScreen = () => {
  const [password, setPassword] = useState("");

  const handleLogin = () => {
    if (password === "12345") {
      router.replace("/notas");
    } else {
      Alert.alert("Error", " Clave incorrectos");
    }
  };

  return (
    <View style={{ flex: 1, justifyContent: "center", padding: 20 }}>
      <Text style={{ fontSize: 18, marginBottom: 15 }}>
        Datos de Login: Clave / 12345
      </Text>

      

      <TextInput
        style={{
          borderWidth: 1,
          borderColor: "gray",
          padding: 8,
          marginBottom: 15,
        }}
        placeholder="Contraseña"
        value={password}
        onChangeText={setPassword}
        secureTextEntry
      />

      <Button title="Ingresar" onPress={handleLogin} />
    </View>
  );
};

export default LoginScreen;