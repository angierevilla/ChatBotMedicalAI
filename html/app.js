// Simulación de datos del paciente (puedes reemplazarlo con una carga desde un archivo JSON)
const pacientes = [
    {
        dni: "12345678",
        nombre: "Juan Pérez",
        historial: "Paciente masculino de 45 años, con antecedentes de hipertensión arterial controlada con medicación. No presenta alergias conocidas. No fumador. Última consulta el 15/03/2023 debido a un episodio de cefalea tensional. Examen físico sin hallazgos relevantes. Se recomienda mantener medicación antihipertensiva y realizar control en 6 meses.",
        medicaciones: [
            {
                nombre: "Losartán",
                dosis: "50 mg/día",
                indicaciones: "Tomar 1 comprimido en la mañana.",
            },
            {
                nombre: "Paracetamol",
                dosis: "500 mg",
                indicaciones: "Tomar 1 comprimido cada 8 horas si presenta dolor de cabeza.",
            }
        ],
        doctor: "Dr. Carlos García",
        fechas: "Consultas: 10/01/2023, 15/03/2023",
    },
    {
        dni: "87654321",
        nombre: "María López",
        historial: "Paciente femenina de 32 años, con antecedentes de asma bronquial. No refiere cirugías previas. Alergia a la penicilina. Última consulta el 05/05/2023 por crisis asmática leve, tratada con broncodilatadores. Control adecuado en el seguimiento pulmonar. Indicaciones de continuar con la medicación actual y monitoreo de síntomas.",
        medicaciones: [
            {
                nombre: "Salbutamol",
                dosis: "100 mcg/inhalación",
                indicaciones: "Inhalar 1 dosis en caso de crisis asmática.",
            },
            {
                nombre: "Budesonida",
                dosis: "200 mcg/inhalación",
                indicaciones: "Inhalar 2 veces al día como tratamiento de mantenimiento.",
            }
        ],
        doctor: "Dra. Laura Fernández",
        fechas: "Consultas: 05/05/2023, 10/07/2023",
    },
    {
        dni: "11223344",
        nombre: "Carlos Martínez",
        historial: "Paciente masculino de 60 años, con antecedentes de diabetes tipo 2 y dislipidemia. No refiere alergias conocidas. Consulta reciente el 12/09/2023 por control de glicemia y perfil lipídico. Se ajusta la dosis de insulina por leve hiperglucemia en ayunas. Se recomienda control nutricional y actividad física moderada.",
        medicaciones: [
            {
                nombre: "Insulina glargina",
                dosis: "10 UI por la noche",
                indicaciones: "Administrar 10 UI subcutáneas antes de dormir.",
            },
            {
                nombre: "Atorvastatina",
                dosis: "20 mg/día",
                indicaciones: "Tomar 1 comprimido por la noche para control de colesterol.",
            }
        ],
        doctor: "Dr. Ricardo Gómez",
        fechas: "Consultas: 12/09/2023, 10/08/2023",
    },
    {
        dni: "99887766",
        nombre: "Ana Rodríguez",
        historial: "Paciente femenina de 28 años, sin antecedentes médicos relevantes. Última consulta el 20/08/2023 por dolor abdominal leve, diagnosticada con síndrome de colon irritable. Se indica tratamiento sintomático y seguimiento en caso de persistencia de síntomas.",
        medicaciones: [
            {
                nombre: "Trimebutina",
                dosis: "200 mg",
                indicaciones: "Tomar 1 comprimido tres veces al día.",
            },
            {
                nombre: "Omeprazol",
                dosis: "20 mg",
                indicaciones: "Tomar 1 comprimido en ayunas por 14 días.",
            }
        ],
        doctor: "Dra. Patricia Ruiz",
        fechas: "Consultas: 20/08/2023",
    }
];


let pacienteActual = null;

document.getElementById('submit-dni').addEventListener('click', function() {
    const dni = document.getElementById('dni-input').value.trim();
    pacienteActual = pacientes.find(paciente => paciente.dni === dni);

    if (pacienteActual) {
        document.getElementById('dni-form').classList.add('hidden');
        document.getElementById('chat-interface').classList.remove('hidden');
        agregarMensaje('Asistente', `Hola ${pacienteActual.nombre}, soy tu medicbot. ¿En qué puedo ayudarte hoy?`);
    } else {
        alert('DNI no encontrado. Por favor, inténtelo de nuevo.');
    }
});

document.getElementById('send-message').addEventListener('click', async function() {
    const mensajeUsuario = document.getElementById('chat-input').value.trim();
    if (mensajeUsuario === '') return;

    agregarMensaje('Tú', mensajeUsuario);
    document.getElementById('chat-input').value = '';

    // Crear mensaje de sistema con la información del paciente
    const systemPrompt = `
Eres un asistente médico que ayuda a los pacientes a entender su historial clínico de forma simple y resumida.

Información del paciente:
- Nombre: ${pacienteActual.nombre}
- Historial: ${pacienteActual.historial}
- Medicaciones: ${pacienteActual.medicaciones.map(medicamento => `- Nombre: ${medicamento.nombre}, Dosis: ${medicamento.dosis}, Indicaciones: ${medicamento.indicaciones}`).join('\n')}
- Doctor: ${pacienteActual.doctor}
- Fechas de atención: ${pacienteActual.fechas}
`;

    // Obtener respuesta de la API de ChatGPT
    const respuesta = await obtenerRespuestaChatGPT(systemPrompt, mensajeUsuario);
    agregarMensaje('Asistente', respuesta);
});

function agregarMensaje(remitente, mensaje) {
    const chatWindow = document.getElementById('chat-window');
    const mensajeDiv = document.createElement('div');
    mensajeDiv.classList.add('mb-2');
    mensajeDiv.classList.add('p-2');
    mensajeDiv.classList.add('rounded');
    mensajeDiv.innerHTML = `<div class="flex flex-row"> <img src="./img/logo.png" alt="${remitente}" width="40" height="40" class="h-8 w-8 rounded-full mb-2"> <div class="text-gray-700 bg-white p-2 rounded-lg">${mensaje}</div><br> </div>`;

    if (remitente === 'Tú') {
        mensajeDiv.innerHTML = `<div class="flex flex-row-reverse"> <img src="https://api.dicebear.com/5.x/initials/svg?seed=${remitente}" alt="${remitente}" width="40" height="40" class="h-8 w-8 rounded-full mb-2"> <div class="text-gray-700 bg-white border p-2 rounded-lg bg-gray-100 mr-2">${mensaje}</div> <br> </div>`;
    }
    chatWindow.appendChild(mensajeDiv);
    chatWindow.scrollTop = chatWindow.scrollHeight;
}

async function obtenerRespuestaChatGPT(systemPrompt, userMessage) {
    const apiKey = 'YOUR_API_KEY'; // Reemplaza con tu clave de API de OpenAI

    try {
        const response = await fetch('https://api.openai.com/v1/chat/completions', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${apiKey}`,
            },
            body: JSON.stringify({
                model: "gpt-3.5-turbo",
                messages: [
                    { role: "system", content: systemPrompt },
                    { role: "user", content: userMessage }
                ],
                max_tokens: 150,
                temperature: 0.7,
            }),
        });

        const data = await response.json();
        return data.choices[0].message.content.trim();
    } catch (error) {
        console.error('Error al comunicarse con la API:', error);
        return 'Lo siento, ha ocurrido un error al procesar tu solicitud.';
    }
}
