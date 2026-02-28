import { router } from "expo-router";
import { useEffect, useState } from "react";
import { Button, FlatList, Text, TextInput, View } from "react-native";

const NotasScreen = () => {
  const [nota, setNota] = useState("");
  const [notas, setNotas] = useState<number[]>([]);
  const [promedio, setPromedio] = useState(0);
  const [error, setError] = useState("");

  const agregarNota = () => {
    const valor = parseFloat(nota);

    if (isNaN(valor)) {
      setError("Debe ingresar un número");
      return;
    }

    if (valor < 0 || valor > 5) {
      setError("La nota debe estar entre 0 y 5");
      return;
    }

    setNotas([...notas, valor]);
    setNota("");
    setError("");
  };

  useEffect(() => {
    if (notas.length === 0) {
      setPromedio(0);
    } else {
      const suma = notas.reduce((acc, item) => acc + item, 0);
      setPromedio(suma / notas.length);
    }
  }, [notas]);

  const limpiarNotas = () => {
    setNotas([]);
    setError("");
  };

  return (
    <View style={{ flex: 1, padding: 20 }}>
      <Text style={{ fontSize: 18, marginBottom: 10 }}>
        Registrar Nota (0 - 5)
      </Text>

      <TextInput
        style={{
          borderWidth: 1,
          borderColor: error ? "red" : "gray",
          padding: 8,
          marginBottom: 5,
        }}
        placeholder="Ingrese nota"
        value={nota}
        onChangeText={setNota}
        keyboardType="numeric"
      />

      {error !== "" && (
        <Text style={{ color: "red", marginBottom: 8 }}>
          {error}
        </Text>
      )}

      <View style={{ marginBottom: 8 }}>
        <Button title="Agregar Nota" onPress={agregarNota} />
      </View>

      <Text style={{ fontSize: 16, marginBottom: 15 }}>
        Promedio: {promedio.toFixed(2)}
      </Text>

      <FlatList
        style={{ marginTop: 10, marginBottom: 10 }}
        data={notas}
        keyExtractor={(_, index) => index.toString()}
        renderItem={({ item, index }) => (
          <Text
            style={{
              padding: 5,
              borderBottomWidth: 1,
              borderColor: "#ccc",
            }}
          >
            Nota {index + 1}: {item}
          </Text>
        )}
      />

      

      <View
        style={{
          flexDirection: "row",
          justifyContent: "space-between",
          marginTop: "auto",
        }}
      >
        <View style={{ flex: 1, marginRight: 5 }}>
          <Button title="Regresar" onPress={() => router.push("/")} />
        </View>

        <View style={{ flex: 1, marginLeft: 5 }}>
          <Button title="Limpiar" onPress={limpiarNotas} />
        </View>
      </View>
    </View>
  );
};

export default NotasScreen;