import Vue from 'vue'
import Vuex from 'vuex'

// Modules
import ecommerceStoreModule from '@/views/apps/e-commerce/eCommerceStoreModule'
import app from './app'
import appConfig from './app-config'
import verticalMenu from './vertical-menu'
import Auth from './modules/auth/index'
import Proceso from './modules/proceso'
import Seguridad from './modules/seguridad'
import Helpers from './modules/helpers'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    app,
    appConfig,
    verticalMenu,
    'app-ecommerce': ecommerceStoreModule,
    auth: Auth,
    proceso: Proceso,
    seguridad: Seguridad,
    helpers: Helpers,
  },
  strict: process.env.DEV,
})
