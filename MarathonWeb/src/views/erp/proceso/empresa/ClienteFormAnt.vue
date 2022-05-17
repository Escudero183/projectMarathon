<template>
  <b-sidebar
    id="element-sidebar"
    :visible="isToggleSidebar"
    bg-variant="white"
    sidebar-class="sidebar-lg"
    shadow
    backdrop
    no-header
    right
    @hidden="resetForm"
    @change="(val) => toggleSidebar(val)"
  >
    <template #default="{ hide }">
      <!-- Header -->
      <div class="d-flex justify-content-between align-items-center content-sidebar-header px-2 py-1">
        <h5 class="mb-0">
          {{ titleForm }}
        </h5>

        <feather-icon
          class="ml-1 cursor-pointer"
          icon="XIcon"
          size="16"
          @click="hide"
        />

      </div>

      <!-- BODY -->
      <validation-observer
        #default="{ handleSubmit }"
        ref="refFormObserver"
      >
        <!-- Form -->
        <b-form
          class="p-2"
          @submit.prevent="handleSubmit(onSubmit)"
          @reset.prevent="resetForm"
        >

          <!-- Tipo de Documento -->
          <b-form-group
            label="Tipo de Documento"
            label-for="tipoDocumento"
          >
            <v-select
              id="tipoDocumento"
              v-model="tipoDocSel"
              label="abreviatura"
              :dir="$store.state.appConfig.isRTL ? 'rtl' : 'ltr'"
              :options="tiposDocumentos"
            />
          </b-form-group>

          <!-- Documento -->
          <validation-provider
            #default="validationContext"
            name="Número de Documento"
            rules="required"
          >
            <b-form-group
              label="Número de Documento"
              label-for="documento"
            >
              <b-form-input
                id="documento"
                v-model="formData.documento"
                autofocus
                :state="getValidationState(validationContext)"
                trim
                placeholder=""
              />

              <b-form-invalid-feedback>
                {{ validationContext.errors[0] }}
              </b-form-invalid-feedback>
            </b-form-group>
          </validation-provider>

          <!-- Nombre o Razón Social -->
          <validation-provider
            #default="validationContext"
            name="Nombre o Razón Social"
            rules="required"
          >
            <b-form-group
              label="Nombre o Razón Social"
              label-for="nombreRazonSocial"
            >
              <b-form-input
                id="nombreRazonSocial"
                v-model="formData.nombreRazonSocial"
                autofocus
                :state="getValidationState(validationContext)"
                trim
                placeholder=""
              />

              <b-form-invalid-feedback>
                {{ validationContext.errors[0] }}
              </b-form-invalid-feedback>
            </b-form-group>
          </validation-provider>

          <!-- Correo -->
          <validation-provider
            #default="validationContext"
            name="Correo"
            rules="required|email"
          >
            <b-form-group
              label="Correo"
              label-for="correo"
            >
              <b-form-input
                id="correo"
                v-model="formData.correo"
                :state="getValidationState(validationContext)"
                trim
              />

              <b-form-invalid-feedback>
                {{ validationContext.errors[0] }}
              </b-form-invalid-feedback>
            </b-form-group>
          </validation-provider>

          <!-- Celular -->
          <validation-provider
            #default="validationContext"
            name="Celular"
            rules="required"
          >
            <b-form-group
              label="Celular"
              label-for="celular"
            >
              <b-form-input
                id="celular"
                v-model="formData.celular"
                :state="getValidationState(validationContext)"
                trim
              />

              <b-form-invalid-feedback>
                {{ validationContext.errors[0] }}
              </b-form-invalid-feedback>
            </b-form-group>
          </validation-provider>

          <!-- País -->
          <b-form-group
            label="País"
            label-for="pais"
          >
            <v-select
              id="pais"
              v-model="formData.pais"
              label="descripcion"
              :dir="$store.state.appConfig.isRTL ? 'rtl' : 'ltr'"
              :options="paises"
              @input="changePais()"
            />
          </b-form-group>

          <b-form-group
            v-if="isPeruano"
            label="Seleccione Departamento"
            label-for="dpto"
          >
            <v-select
              v-model="dptoSel"
              :reduce="m => m.idUbigeo"
              label="descripcion"
              :dir="$store.state.appConfig.isRTL ? 'rtl' : 'ltr'"
              :options="dptos"
              @input="getUbigeos('prov', dptoSel.slice(0, 2))"
            />
          </b-form-group>
          <b-form-group
            v-if="isPeruano"
            label="Seleccione Provincia"
            label-for="prov"
          >
            <v-select
              v-model="provSel"
              :reduce="m => m.idUbigeo"
              label="descripcion"
              :dir="$store.state.appConfig.isRTL ? 'rtl' : 'ltr'"
              :options="provs"
              @input="getUbigeos('dist', provSel.slice(0, 4))"
            />
          </b-form-group>
          <b-form-group
            v-if="isPeruano"
            label="Seleccione Distrito"
            label-for="dist"
          >
            <v-select
              v-model="distSel"
              :reduce="m => m.idUbigeo"
              label="descripcion"
              :dir="$store.state.appConfig.isRTL ? 'rtl' : 'ltr'"
              :options="dists"
            />
          </b-form-group>

          <!-- Dirección -->
          <validation-provider
            #default="validationContext"
            name="Dirección"
            rules="required"
          >
            <b-form-group
              label="Dirección"
              label-for="direccion"
            >
              <b-form-input
                id="direccion"
                v-model="formData.direccion"
                :state="getValidationState(validationContext)"
                trim
              />

              <b-form-invalid-feedback>
                {{ validationContext.errors[0] }}
              </b-form-invalid-feedback>
            </b-form-group>
          </validation-provider>

          <!-- Form Actions -->
          <div class="d-flex mt-2">
            <b-button
              v-ripple.400="'rgba(255, 255, 255, 0.15)'"
              variant="primary"
              class="mr-2"
              type="submit"
            >
              Guardar
            </b-button>
            <b-button
              v-ripple.400="'rgba(186, 191, 199, 0.15)'"
              type="button"
              variant="outline-secondary"
              @click="hide"
            >
              Cancelar
            </b-button>
          </div>

        </b-form>
      </validation-observer>
    </template>
  </b-sidebar>
</template>

<script>
import {
  BSidebar, BForm, BFormGroup, BFormInput, BFormInvalidFeedback, BButton,
} from 'bootstrap-vue'
import { ValidationProvider, ValidationObserver } from 'vee-validate'
import { ref } from '@vue/composition-api'
import { required } from '@validations'
import formValidation from '@core/comp-functions/forms/form-validation'
import Ripple from 'vue-ripple-directive'
import vSelect from 'vue-select'
import store from '@/store'

export default {
  components: {
    BSidebar,
    BForm,
    BFormGroup,
    BFormInput,
    BFormInvalidFeedback,
    BButton,
    vSelect,

    // Form Validation
    ValidationProvider,
    ValidationObserver,
  },
  directives: {
    Ripple,
  },
  model: {
    prop: 'isToggleSidebar',
    event: 'update:is-toggle-sidebar',
  },
  props: {
    isToggleSidebar: {
      type: Boolean,
      required: true,
    },
    dataEdit: {
      type: Object,
      required: false,
      default: null,
    },
    formType: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      required,
    }
  },
  setup(props, { emit }) {
    const titleForm = ref('')
    const tiposDocumentos = ref([])
    const empresa = ref({
      idEmpresa: 3,
    })
    const blankData = {
      documento: '',
      correo: '',
      celular: '',
      sexo: '',
      nombreRazonSocial: '',
    }
    const paises = ref([])
    const dptoSel = ref('220000')
    const provSel = ref(null)
    const distSel = ref(null)
    const dptos = ref([])
    const provs = ref([])
    const dists = ref([])
    const isPeruano = ref(true)
    const tipoDocSel = ref(null)

    const formData = ref(JSON.parse(JSON.stringify(blankData)))
    const resetData = () => {
      formData.value = JSON.parse(JSON.stringify(blankData))
    }

    const changePais = () => {
      if (formData.value.pais && formData.value.pais.idPais === '9589') isPeruano.value = true
      else isPeruano.value = false
    }

    const getTiposDocumentos = async () => {
      await store
        .dispatch('administracion/TIPO_DOCUMENTO_FIND_ALL', {
          tipo: 'cmb',
          page: 1,
          limit: 10,
          query: '',
          sortBy: 'idTipoDocumento%7CASC',
        })
        .then(response => {
          tiposDocumentos.value = response
          tipoDocSel.value = tiposDocumentos.value.find(tp => tp.idTipoDocumento === 1)
        })
        .catch(error => {
          emit('error-data', error)
          emit('update:is-toggle-sidebar', false)
        })
    }

    const getPaises = async () => {
      await store
        .dispatch('administracion/PAIS_FIND_ALL')
        .then(response => {
          paises.value = response
          formData.value.pais = paises.value.find(p => p.idPais === '9589')
        })
        .catch(error => {
          emit('error-data', error)
          emit('update:is-toggle-sidebar', false)
        })
    }

    const getUbigeos = async (type, codUbigeo) => {
      await store
        .dispatch('administracion/UBIGEO_FIND_ALL', {
          get: type,
          codUbigeo,
        })
        .then(response => {
          if (type === 'dpto') {
            dptos.value = response
            provSel.value = null
            distSel.value = null
            provs.value = []
            dists.value = []
          }
          if (type === 'prov') {
            provs.value = response
            provSel.value = null
            distSel.value = null
            dists.value = []
          }
          if (type === 'dist') {
            dists.value = response
          }
        })
        .catch(error => {
          emit('error-data', error)
        })
    }

    const onSubmit = () => {
      /* VALIDACIONES */
      if (tipoDocSel.value === null) {
        emit('error-data', { response: { data: { message: 'Debe seleccionar un tipo de documento' } } })
        return
      }

      if (tipoDocSel.value.idTipoDocumento !== 0 && (formData.value.documento.length !== tipoDocSel.value.digitos)) {
        emit('error-data', { response: { data: { message: 'Debe ingresar un número de tipo de documento válido' } } })
        return
      }

      let service = 'comercial/CLIENTE_CREATE'
      if (props.formType === 'edit') {
        service = 'comercial/CLIENTE_UPDATE'
      }

      if (formData.value.pais && formData.value.pais.idPais !== '9589') {
        formData.value.ubigeo = {
          idUbigeo: '000000',
        }
      } else {
        formData.value.ubigeo = {
          idUbigeo: distSel.value,
        }
      }
      formData.value.tipoDocumento = tipoDocSel.value

      formData.value.empresa = empresa.value
      store.dispatch(service, formData.value)
        .then(response => {
          emit('refetch-data', response)
          emit('update:is-toggle-sidebar', false)
        })
        .catch(error => {
          emit('error-data', error)
          emit('update:is-toggle-sidebar', false)
        })
    }

    const toggleSidebar = async val => {
      emit('update:is-toggle-sidebar', val)
      getPaises()
      getTiposDocumentos()
      isPeruano.value = true
      if (props.formType === 'new') {
        titleForm.value = 'Agregar Cliente'
        getUbigeos('dpto', '')
        getUbigeos('prov', dptoSel.value.slice(0, 2))
      }
      if (props.formType === 'edit') {
        formData.value = {
          idCliente: props.dataEdit.idCliente,
          nombreRazonSocial: props.dataEdit.nombreRazonSocial,
          documento: props.dataEdit.documento,
          correo: props.dataEdit.correo,
          celular: props.dataEdit.celular,
          direccion: props.dataEdit.direccion,
          ubigeo: props.dataEdit.ubigeo,
          empresa: props.dataEdit.empresa,
          fechaNacimiento: props.dataEdit.fechaNacimiento,
          pais: props.dataEdit.pais,
          sexo: props.dataEdit.sexo,
          estado: props.dataEdit.estado,
        }

        tipoDocSel.value = props.dataEdit.tipoDocumento

        if (formData.value.pais && formData.value.pais.idPais === '9589') {
          dptoSel.value = `${props.dataEdit.ubigeo.idUbigeo.slice(0, 2)}0000`
          await getUbigeos('dpto', '')
          await getUbigeos('prov', props.dataEdit.ubigeo.idUbigeo.slice(0, 2))
          provSel.value = `${props.dataEdit.ubigeo.idUbigeo.slice(0, 4)}00`
          await getUbigeos('dist', props.dataEdit.ubigeo.idUbigeo.slice(0, 4))
          distSel.value = props.dataEdit.ubigeo.idUbigeo
        } else {
          isPeruano.value = false
        }
        titleForm.value = 'Editar Cliente'
      }
    }

    const {
      refFormObserver,
      getValidationState,
      resetForm,
    } = formValidation(resetData)

    return {
      formData,
      onSubmit,
      refFormObserver,
      getValidationState,
      resetForm,
      toggleSidebar,
      titleForm,
      tiposDocumentos,
      empresa,
      paises,
      dptoSel,
      provSel,
      distSel,
      dptos,
      provs,
      dists,
      getUbigeos,
      changePais,
      isPeruano,
      tipoDocSel,
    }
  },
}
</script>

<style lang="scss">
@import '@core/scss/vue/libs/vue-select.scss';

#element-sidebar {
  .vs__dropdown-menu {
    max-height: 200px !important;
  }
}
</style>
