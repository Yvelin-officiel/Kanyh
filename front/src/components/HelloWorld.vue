<template>
  <div class="bg-light-bg h-screen p-8 font-inter">
    <div class="max-w-4xl mx-auto">
      <header class="mb-8">
        <h1 class="text-3xl">Design system preview</h1>
        <p class="text-lg mt-4">Typographie, couleurs, boutons — prévisualisation</p>
      </header>

      <!-- Typography -->
      <section class="mb-8">
        <p class="mt-2 titre">H1 - Titre principal (Cinzel) - .titre</p>

        <h3 class="mt-6 text-lg font-inter">Paragraphes (Inter)</h3>
        <p class="mt-2 text-lg font-inter text-txt-primary">Exemple de texte en Inter avec couleur txt-primary.</p>
        <p class="mt-2 text-lg font-inter text-txt-secondary">Exemple de texte en Inter avec couleur txt-secondary.</p>
      </section>

      <!-- Buttons -->
      <section class="mb-8">
        <h2 class="text-2xl font-cinzel">Boutons</h2>
        <div class="mt-4 flex gap-4">
          <button class="btn-primary">btn-primary</button>
          <button class="btn-secondary">btn-secondary</button>
        </div>
      </section>

      <!-- Color swatches -->
      <section class="mb-8">
        <h2 class="text-2xl font-cinzel">Couleurs</h2>
        <div class="mt-4 flex items-center gap-4 ">
          <div class="flex flex-col items-center text-center">
            <div class=" bg-primary w-10 h-10"></div>
            <div class="mt-2">Primary<br><small>#C5A059</small></div>
          </div>

          <div class="flex flex-col items-center text-center">
            <div class=" bg-primary-dark w-10 h-10"></div>
            <div class="mt-2">Primary dark<br><small>#A17C3B</small></div>
          </div>

          <div class="flex flex-col items-center text-center">
            <div class=" bg-secondary w-10 h-10"></div>
            <div class="mt-2">Secondary<br><small>#2D6A4F</small></div>
          </div>

          <div class="flex flex-col items-center text-center ">
            <div class=" bg-secondary-dark w-10 h-10"></div>
            <div class="mt-2">Secondary dark<br><small>#1B4332</small></div>
          </div>

          <div class="flex flex-col items-center text-center">
            <div class="bg-accent w-10 h-10"></div>
            <div class="mt-2">Accent<br><small>#B02A37</small></div>
          </div>
          <div class="flex flex-col items-center text-center"></div>
        </div>
      </section>

      <section class="mb-8">
        <h2 class="text-2xl font-cinzel mb-4">Input</h2>


        <div class="flex flex-col bg-[#f3e9dc] mt-8">
        <label for="" class="input-label">
          Label
        </label>

        <input
          id=""
          type="text"
          placeholder="Placeholder"
          class="input-field"
        />
      </div>

      </section>

    </div>
    </div>
</template>

<script>
export default {
  name: 'HelloWorldPreview'
}
</script>
