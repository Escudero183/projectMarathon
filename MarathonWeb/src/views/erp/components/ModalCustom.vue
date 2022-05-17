<template>
  <div
    v-if="dataInfo"
  >
    <b-modal
      v-model="modalshow"
      :title="dataInfo.titleModal"
      :hide-footer="true"
      no-close-on-backdrop
      no-close-on-esc
      size="lg"
      :busy="isBusy"
      :ok-disabled="true"
    >
      <b-alert
        variant="primary"
        show
      >
        <div class="alert-body">
          <feather-icon
            class="mr-25"
            :icon="dataInfo.alertIcon"
          />
          <span class="ml-25"><strong>{{ dataInfo.alertMsgTitle }}</strong> {{ dataInfo.alertMsgSubTitle }}</span>
        </div>
      </b-alert>

      <b-form-group
        label="Stock Actual"
        label-for="stockActual"
      >
        <b-form-spinbutton
          id="sb-maxmin"
          v-model="formData.cantidadModificar"
          min="-10"
          max="10"
        />
      </b-form-group>

      <b-form-group
        label="Stock Actual"
        label-for="stockActual"
      >
        <b-input-group
          append="UND"
        >
          <b-form-input
            id="stockActual"
            v-model="formData.stockActual"
            autofocus
            trim
            placeholder=""
          />
        </b-input-group>
      </b-form-group>
    </b-modal>
  </div>
</template>

<script>
import {
  BAlert, BModal, BFormGroup, BFormInput, BInputGroup, BFormSpinbutton,
} from 'bootstrap-vue'
import {
  ref,
  // ref, watch, computed, onUnmounted,
} from '@vue/composition-api'
import Ripple from 'vue-ripple-directive'
import config from '@/services/config'
import { showToast, showError } from '@/helpers'

export default {
  components: {
    BAlert,
    BModal,
    BFormGroup,
    BFormInput,
    BInputGroup,
    BFormSpinbutton,
  },
  directives: {
    Ripple,
  },
  props: {
    dataInfo: {
      type: Object,
      required: false,
      default: null,
    },
  },
  data() {
    return {
      idEmpresa: 0,
      formData: {
        cantidadModificar: 0,
      },
    }
  },
  setup() {
    const modalshow = ref(false)
    const isBusy = ref(false)
    const { baseURL } = config
    const pathMultimedia = ref(`${baseURL}api-checkapp/api/v1/multimedia`)

    const okMdl = () => {
      console.log('entramos al okMdl')
    }

    const openModal = () => {
      modalshow.value = true
    }

    return {
      // Customs
      modalshow,
      okMdl,
      openModal,
      isBusy,
      pathMultimedia,
    }
  },
  methods: {
    showToast,
    showError,
  },
}
</script>

<style>
.modal-header{
  align-items: center;
}
.filepond--item {
    width: calc(50% - 0.5em);
}
@media (min-width: 30em) {
    .filepond--item {
        width: calc(50% - 0.5em);
    }
}
@media (min-width: 50em) {
    .filepond--item {
        width: calc(33.33% - 0.5em);
    }
}
</style>
