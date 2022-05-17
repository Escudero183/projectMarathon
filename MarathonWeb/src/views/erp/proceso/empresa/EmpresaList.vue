<template>
  <div>
    <empresa-form
      :is-toggle-sidebar.sync="isToggleSidebar"
      :form-type="formType"
      :data-edit="dataEdit"
      @refetch-data="refetchData"
      @error-data="showError"
    />

    <!-- Table Container Card -->
    <b-card
      no-body
      class="mb-0"
    >

      <div class="m-2">
        <!-- Table Top -->
        <b-row>
          <!-- Per Page -->
          <b-col
            cols="12"
            md="6"
            class="d-flex align-items-center justify-content-start mb-1 mb-md-0"
          >
            <label>Mostrando</label>
            <v-select
              v-model="perPage"
              :dir="$store.state.appConfig.isRTL ? 'rtl' : 'ltr'"
              :options="pageOptions"
              :clearable="false"
              class="per-page-selector d-inline-block mx-50"
            />
            <label>registros</label>
          </b-col>

          <!-- Search -->
          <b-col
            cols="12"
            md="6"
          >
            <div class="d-flex align-items-center justify-content-end">
              <b-form-input
                v-model="query"
                class="d-inline-block mr-1"
                placeholder="Buscar..."
              />
              <b-button
                variant="primary"
                @click="openForm('new')"
              >
                <span class="text-nowrap">Agregar</span>
              </b-button>
            </div>
          </b-col>
        </b-row>

      </div>

      <b-table
        ref="refUserListTable"
        class="position-relative"
        :items="items"
        responsive
        :fields="fields"
        primary-key="id"
        :sort-by.sync="sortBy"
        show-empty
        empty-text="No se encontraron registros"
        :busy="isBusy"
      >
        <template #table-busy>
          <div class="text-center text-primary my-2">
            <b-spinner
              variant="primary"
              class="align-middle"
            />
            <strong class="ml-1">Cargando...</strong>
          </div>
        </template>
        <template #cell(razonSocial)="data">
          <b-media vertical-align="center">
            <template #aside>
              <b-avatar
                size="32"
                :src="`data:image/jpeg;base64,${data.item.foto}`"
                :text="avatarText(`${data.item.razonSocial}`)"
                :variant="`light-${getColorAvatar()}`"
              />
            </template>
            <span class="font-weight-bold d-block text-nowrap">
              {{ data.item.razonSocial }}
            </span>
            <small class="text-muted">{{ data.item.representanteLegal }}</small>
            <small class="text-muted d-block ">RUC {{ data.item.documento }}</small>
          </b-media>
        </template>

        <template #cell(estadoSunat)="data">
          <template v-if="data.item.estadoSunat === 'ACTIVO'">
            <b-badge
              pill
              variant="light-success"
            >
              {{ data.item.estadoSunat }}
            </b-badge>
          </template>
          <template v-else>
            <b-badge
              pill
              variant="light-danger"
            >
              {{ data.item.estadoSunat }}
            </b-badge>
          </template>
        </template>

        <template #cell(departamento)="data">
          <b-media vertical-align="center">
            <span class="font-weight-bold d-block font-small-3">
              {{ data.item.direccion }}
            </span>
            <small class="text-muted font-small-2">{{ data.item.departamento }} / {{ data.item.provincia }} / {{ data.item.distrito }}</small>
          </b-media>
        </template>

        <!-- Column: Actions -->
        <template #cell(acciones)="data">
          <b-dropdown
            variant="link"
            no-caret
            :right="$store.state.appConfig.isRTL"
          >

            <template #button-content>
              <feather-icon
                icon="MoreVerticalIcon"
                size="16"
                class="align-middle text-body"
              />
            </template>

            <b-dropdown-item @click="getEdit(data)">
              <feather-icon icon="EditIcon" />
              <span class="align-middle ml-50">Editar</span>
            </b-dropdown-item>

            <b-dropdown-item @click="getDelete(data)">
              <feather-icon icon="TrashIcon" />
              <span class="align-middle ml-50">Eliminar</span>
            </b-dropdown-item>
          </b-dropdown>
        </template>

      </b-table>
      <div class="mx-2 mb-2">
        <b-row>

          <b-col
            cols="12"
            sm="6"
            class="d-flex align-items-center justify-content-center justify-content-sm-start"
          >
            <span class="text-muted">Mostrando {{ from }} a {{ to }} de {{ of }} registros</span>
          </b-col>
          <!-- Pagination -->
          <b-col
            cols="12"
            sm="6"
            class="d-flex align-items-center justify-content-center justify-content-sm-end"
          >

            <b-pagination
              v-model="page"
              :total-rows="totalItems"
              :per-page="perPage"
              first-number
              last-number
              class="mb-0 mt-1 mt-sm-0"
              prev-class="prev-item"
              next-class="next-item"
            >
              <template #prev-text>
                <feather-icon
                  icon="ChevronLeftIcon"
                  size="18"
                />
              </template>
              <template #next-text>
                <feather-icon
                  icon="ChevronRightIcon"
                  size="18"
                />
              </template>
            </b-pagination>

          </b-col>

        </b-row>
      </div>
    </b-card>
  </div>
</template>

<script>
import {
  BCard, BRow, BCol, BFormInput, BButton, BTable, BBadge, BDropdown, BDropdownItem, BPagination, BSpinner, BAvatar, BMedia, VBTooltip,
} from 'bootstrap-vue'
import vSelect from 'vue-select'
import store from '@/store'
import { showToast, showError } from '@/helpers'
import { avatarText } from '@core/utils/filter'
import EmpresaForm from './EmpresaForm.vue'

export default {
  components: {
    EmpresaForm,
    BCard,
    BRow,
    BCol,
    BFormInput,
    BButton,
    BTable,
    BBadge,
    BDropdown,
    BDropdownItem,
    BPagination,

    vSelect,
    BSpinner,
    BAvatar,
    BMedia,
  },
  directives: {
    'b-tooltip': VBTooltip,
  },
  data: () => ({
    isBusy: false,
    idEmpresa: 3,
    isToggleSidebar: false,
    dataEdit: {},
    formType: '',
    page: 1,
    perPage: 10,
    totalRows: 0,
    totalItems: 0,
    totalPage: 0,
    from: 0,
    to: 0,
    of: 0,
    pageOptions: [10, 20, 50],
    sortBy: null,
    sortDesc: false,
    sortDirection: 'asc',
    query: '',
    items: [],
    fields: [
      { key: 'idEmpresa', sortable: true, thClass: 'd-none', tdClass: 'd-none' },
      { key: 'razonSocial', label: 'Empresa', sortable: true },
      { key: 'departamento', label: 'Ubicación', sortable: true, tdClass: 'w200', thClass: 'w200' },
      { key: 'estadoSunat', label: 'Sunat', sortable: true, tdClass: 'text-center', thClass: 'text-center' },
      { key: 'acciones' },
    ],
  }),
  watch: {
    perPage: {
      handler() {
        this.listarRegistros()
      },
    },
    page: {
      handler() {
        this.listarRegistros()
      },
    },
    query: {
      handler() {
        this.listarRegistros()
      },
    },
  },
  mounted() {
    this.listarRegistros()
  },
  methods: {
    async listarRegistros() {
      this.isBusy = true
      await store
        .dispatch('proceso/EMPRESA_FIND_ALL', {
          limit: this.perPage,
          query: this.query,
          page: this.page,
          sortBy: 'idEmpresa%7CDESC',
        })
        .then(response => {
          this.items = response.items
          this.totalRows = response.totalRows
          this.totalItems = response.totalItems
          this.totalPage = response.totalPage
          this.from = this.perPage * (this.page - 1) + (this.totalRows ? 1 : 0)
          this.to = this.perPage * (this.page - 1) + this.totalRows
          this.of = this.totalItems
        })
        .catch(error => {
          this.showToast('Ha ocurrido un error', error, 'danger')
        })
      this.isBusy = false
    },
    openForm(type) {
      this.formType = type
      this.isToggleSidebar = true
    },
    getEdit(data) {
      this.formType = 'edit'
      this.isToggleSidebar = true
      console.log(data.item)
      this.dataEdit = data.item
    },
    getDelete(data) {
      this.$swal({
        title: 'Está seguro?',
        text: 'Va eliminar un registro!',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Sí',
        cancelButtonText: 'No',
        customClass: {
          confirmButton: 'btn btn-danger',
          cancelButton: 'btn btn-primary ml-1',
        },
        buttonsStyling: false,
      }).then(result => {
        if (result.value) {
          store
            .dispatch('proceso/EMPRESA_DELETE', {
              id: data.item.idEmpresa,
            })
            .then(response => {
              this.listarRegistros()
              this.showToast('Operación Extitosa', 'Ha Eliminado correctamente el registro.', 'success')
              return response
            })
            .catch(error => {
              this.showToast('Ha ocurrido un error', error, 'danger')
            })
        }
      })
    },
    refetchData(response) {
      let message = 'El proceso se ha ejecutado satisfactoriamente.'
      if (response.message) {
        message = response.message
      }
      this.showToast('Operación Extitosa', message, 'success')
      this.listarRegistros()
    },
    getColorAvatar() {
      const max = 7
      const min = 1
      const status = Math.floor(Math.random() * ((max + 1) - min) + min)
      if (status === 1) return 'primary'
      if (status === 2) return 'danger'
      if (status === 3) return 'secondary'
      if (status === 4) return 'warning'
      if (status === 5) return 'info'
      if (status === 6) return 'success'
      return 'primary'
    },
    avatarText,
    showToast,
    showError,
  },
}
</script>

<style lang="scss" scoped>
.per-page-selector {
  width: 90px;
}
</style>

<style lang="scss">
@import '@core/scss/vue/libs/vue-select.scss';
</style>

<style>
.w200{
  width: 350px !important;
}
</style>
